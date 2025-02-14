package com.paicbd.smsc.interpreter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.paicbd.smsc.dto.MessageEvent;
import com.paicbd.smsc.dto.UtilsRecords;
import com.paicbd.smsc.exception.RTException;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PayloadInterpreter {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Map<Class<?>, UnaryOperator<Object>> CONVERTERS = new HashMap<>();

    static {
        CONVERTERS.put(String.class, value -> "null".equals(value) ? null : value.toString());
        CONVERTERS.put(int.class, value -> Integer.parseInt(value.toString()));
        CONVERTERS.put(Integer.class, value -> parseInt(value.toString()));
        CONVERTERS.put(long.class, value -> Long.parseLong(value.toString()));
        CONVERTERS.put(Long.class, value -> Long.parseLong(value.toString()));
        CONVERTERS.put(byte.class, value -> Byte.parseByte(value.toString()));
        CONVERTERS.put(boolean.class, value -> parseBoolean(value.toString()));
        CONVERTERS.put(Boolean.class, value -> parseBoolean(value.toString()));
        CONVERTERS.put(Map.class, value -> value);
    }

    @Generated
    private PayloadInterpreter() {
        throw new IllegalStateException("Utility class");
    }

    // ************************************************************************
    // Related with body for send
    // ************************************************************************
    public static String interpretPayloadForSend(String payload, MessageEvent event, PayloadFormat type) throws JsonProcessingException {
        Pattern patternXmlList = Pattern.compile("\\{\\{(\\w+(?:\\.\\w+){0,5}):([A-Z]+)(?::(\\w+))?}}");
        Matcher matcher = patternXmlList.matcher(payload);

        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String propertyName = matcher.group(1);
            DataType propertyType = DataType.valueOf(matcher.group(2));
            String elementName = null;
            if (propertyType.equals(DataType.LIST) && PayloadFormat.XML.equals(type)) {
                elementName = Optional.ofNullable(matcher.group(3)).orElse("optionalParam");
            }

            Object value = getProperty(event, propertyName);
            String replacement;
            if (value != null) {
                replacement = formatValue(value, propertyType, type, elementName);
            } else {
                replacement = "";
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        matcher.appendTail(result);
        if (PayloadFormat.JSON.equals(type)) {
            result.append("}");
        }

        return result.toString();
    }

    private static String formatValue(Object value, DataType type, PayloadFormat payloadType, String elementName) throws JsonProcessingException {
        log.debug("Formatting value: {} of type: {} as: {}", value, type, payloadType);
        boolean isXml = PayloadFormat.XML.equals(payloadType);

        return switch (type) {
            case STRING -> isXml ? value.toString() : "\"" + value + "\"";
            case HEX -> isXml
                    ? String.format("0x%02X", Integer.parseInt(value.toString()))
                    : String.format("\"0x%02X\"", Integer.parseInt(value.toString()));
            case INT, DOUBLE, BYTE, LONG, MAP -> value.toString();
            case BOOLEAN -> formatBoolean(value);
            case LIST -> formatList(value, payloadType, elementName);
        };
    }

    private static String formatBoolean(Object value) {
        log.debug("Formatting BOOLEAN value: {}", value);
        if (value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Integer integer) {
            return integer == 1 ? "true" : "false";
        } else {
            log.error("Unsupported type for BOOLEAN: {}", value.getClass().getSimpleName());
            return "";
        }
    }

    private static String formatList(Object value, PayloadFormat payloadType, String elementName) throws JsonProcessingException {
        log.debug("Formatting LIST value: {} as: {}", value, payloadType);
        if (PayloadFormat.XML.equals(payloadType) && value instanceof List<?> list) {
            log.debug("Formatting LIST value: {} as XML", value);
            return formatAsXmlList(list, elementName);
        }

        ObjectMapper mapper = PayloadFormat.XML.equals(payloadType) ? new XmlMapper() : new ObjectMapper();
        return mapper.writeValueAsString(value);
    }

    private static String formatAsXmlList(List<?> list, String elementName) {
        StringBuilder result = new StringBuilder();
        for (Object item : list) {
            result.append(formatAsXmlItem(item, elementName));
        }
        return result.toString();
    }

    private static String formatAsXmlItem(Object item, String elementName) {
        StringBuilder itemXml = new StringBuilder();
        itemXml.append("<").append(elementName).append(">");

        for (Field field : item.getClass().getDeclaredFields()) {
            itemXml.append(formatAsXmlField(item, field));
        }

        itemXml.append("</").append(elementName).append(">");
        return itemXml.toString();
    }

    private static String formatAsXmlField(Object item, Field field) {
        try {
            log.debug("Accessing field: {}", field.getName());
            var lookup = MethodHandles.privateLookupIn(field.getDeclaringClass(), MethodHandles.lookup());
            var fieldHandle = lookup.unreflectGetter(field);
            Object fieldValue = fieldHandle.invoke(item);
            String fieldName = field.getName();
            if (fieldValue != null) {
                return "<" + fieldName + ">" + fieldValue + "</" + fieldName + ">";
            } else {
                return "<" + fieldName + "></" + fieldName + ">";
            }
        } catch (Throwable e) {
            log.error("An error occurred while accessing field: {}", field.getName(), e);
            return null;
        }
    }

    private static Object getProperty(MessageEvent event, String propertyName) {
        log.debug("Getting property: {} from event: {}", propertyName, event);
        try {
            Field field = event.getClass().getDeclaredField(propertyName.split("\\.")[0]);
            if (field.getType().equals(Map.class)) {
                Method method = event.getClass().getMethod("get" + capitalize(propertyName.split("\\.")[0]));
                Object mapObject = method.invoke(event);

                if (mapObject instanceof Map<?, ?> map) {
                    propertyName = propertyName.substring(propertyName.indexOf(".") + 1);
                    return getNestedValue(map, propertyName);
                }
            }

            if (field.getType().equals(boolean.class)) {
                String methodName = propertyName.startsWith("is") ? propertyName : "is" + capitalize(propertyName);
                return MessageEvent.class.getMethod(methodName).invoke(event);
            }

            return MessageEvent.class.getMethod("get" + capitalize(propertyName)).invoke(event);
        } catch (Exception e) {
            log.error("Failed to get property: {}", propertyName, e);
            return "";
        }
    }

    private static Object getNestedValue(Map<?, ?> map, String propertyName) {
        String[] keys = propertyName.split("\\.");

        Object current = map;
        for (String key : keys) {
            if (current instanceof Map<?, ?> currentMap) {
                current = currentMap.get(key);
            } else {
                log.error("Invalid property path: {}", propertyName);
                return "";
            }
        }
        return current;
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    // ************************************************************************
    // Related with received body
    // ************************************************************************
    public static void interpreterPayloadForReceive(String payload, String fieldsMapper, MessageEvent event, PayloadFormat type) throws JsonProcessingException {
        Pattern patternXmlList = Pattern.compile("\\{\\{(\\w+(?:\\.\\w+){0,5}):([A-Z]+)(?::(\\w+))?}}");
        JsonNode payloadAsNode;
        JsonNode fieldsMapperAsNode;

        if (PayloadFormat.XML.equals(type)) {
            payloadAsNode = ObjectsInterpreter.xmlStringToJsonNode(payload);
            fieldsMapperAsNode = ObjectsInterpreter.xmlStringToJsonNode(fieldsMapper);
        } else {
            payloadAsNode = mapper.readTree(payload);
            fieldsMapperAsNode = mapper.readTree(fieldsMapper);
        }

        Assert.notNull(payloadAsNode, "Payload node is null");
        Assert.notNull(fieldsMapperAsNode, "Fields mapper node is null");
        processNode(fieldsMapperAsNode, payloadAsNode, event, patternXmlList, type);
    }

    private static void processNode(
            JsonNode mapperNode, JsonNode payloadNode, MessageEvent event, Pattern pattern, PayloadFormat type) {
        if (!mapperNode.isObject()) {
            return;
        }

        Iterator<Map.Entry<String, JsonNode>> fields = mapperNode.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            processField(field, payloadNode, event, pattern, type);
        }
    }

    @Generated
    private static void processField(
            Map.Entry<String, JsonNode> field, JsonNode payloadNode, MessageEvent event, Pattern pattern, PayloadFormat type) {
        String fieldName = field.getKey();
        JsonNode mapperFieldValue = field.getValue();
        JsonNode payloadFieldValue = payloadNode.get(fieldName);

        if (mapperFieldValue.isObject()) {
            processNode(mapperFieldValue, payloadFieldValue, event, pattern, type);
        } else if (mapperFieldValue.isTextual()) {
            processTextualField(mapperFieldValue.asText(), payloadFieldValue, event, pattern, type);
        }
    }

    private static void processTextualField(
            String mapperTextValue, JsonNode payloadFieldValue, MessageEvent event, Pattern pattern, PayloadFormat type) {
        Matcher matcher = pattern.matcher(mapperTextValue);
        if (matcher.matches()) {
            String propertyName = matcher.group(1);
            DataType propertyType = DataType.valueOf(matcher.group(2));

            String elementName = null;
            if (PayloadFormat.XML.equals(type) && "LIST".equals(propertyType.name())) {
                elementName = matcher.group(3);
            }

            handleMatchedField(payloadFieldValue, event, propertyName, propertyType, elementName);
        }
    }

    private static void handleMatchedField(
            JsonNode payloadFieldValue, MessageEvent event, String propertyName, DataType propertyType, String elementName) {
        if (payloadFieldValue == null) {
            return;
        }

        String payloadValue = payloadFieldValue.asText();
        Object convertedValue = null;
        if (payloadValue.isEmpty()) {
            payloadValue = payloadFieldValue.toString();
            try {
                if (DataType.LIST.equals(propertyType)) {
                    convertedValue = processOptionalParamsValue(payloadFieldValue, elementName);
                } else {
                    convertedValue = mapper.readValue(payloadValue, Object.class);
                }
            } catch (JsonProcessingException e) {
                log.error("Failed to process optional parameters value: {}", payloadValue, e);
            }
        } else {
            convertedValue = convertValue(payloadValue, propertyType);
        }

        setEventProperty(event, propertyName, convertedValue);
    }

    private static List<UtilsRecords.OptionalParameter> processOptionalParamsValue(JsonNode optParamNode, String elementName) throws JsonProcessingException {
        JsonNode optParamsArray = (elementName != null) ? optParamNode.get(elementName) : optParamNode;
        if (optParamsArray == null || optParamsArray.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            List<Map<String, String>> mapList = mapper.readValue(optParamsArray.traverse(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            return mapList.stream()
                    .map(UtilsRecords.OptionalParameter::new)
                    .toList();
        } catch (Exception e) {
            log.error("Failed to process optional parameters value: {}", optParamNode, e);
            return Collections.emptyList();
        }
    }


    private static void setEventProperty(MessageEvent event, String propertyName, Object value) {
        try {
            Field field = event.getClass().getDeclaredField(propertyName);
            Class<?> fieldType = field.getType();

            if (fieldType.equals(boolean.class) && propertyName.startsWith("is")) {
                propertyName = propertyName.substring(2);
            }

            String setterName = "set" + capitalize(propertyName);
            String finalPropertyName = propertyName;
            Method setter = Arrays.stream(event.getClass().getMethods())
                    .filter(method -> method.getName().equals(setterName) && method.getParameterCount() == 1)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Setter not found for property: " + finalPropertyName));

            Object convertedValue = convertToTargetType(value, fieldType);
            setter.invoke(event, convertedValue);
        } catch (Exception e) {
            throw new RTException("Failed to set property: " + propertyName, e);
        }
    }

    private static Object convertValue(String value, DataType type) {
        return switch (type) {
            case HEX -> Integer.parseInt(value.replace("0x", ""), 16);
            case INT -> Integer.parseInt(value);
            case LONG -> Long.parseLong(value);
            case BYTE -> Byte.parseByte(value);
            case BOOLEAN -> Boolean.parseBoolean(value);
            case LIST -> Arrays.asList(value.split(","));
            default -> value;
        };
    }

    public static Object convertToTargetType(Object value, Class<?> targetType) {
        try {
            UnaryOperator<Object> converter = CONVERTERS.get(targetType);
            if (converter != null) {
                return converter.apply(value);
            }
            return value;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert value '" + value + "' to type: " + targetType, e);
        }
    }

    private static int parseInt(String value) {
        value = value.trim();
        if (value.startsWith("0x") || value.startsWith("0X")) {
            return Integer.parseInt(value.substring(2), 16);
        }

        return switch (value.toLowerCase()) {
            case "true" -> 1;
            case "false" -> 0;
            default -> Integer.parseInt(value);
        };
    }

    private static boolean parseBoolean(String value) {
        if (value.startsWith("0x")) {
            return Integer.parseInt(value.replace("0x", ""), 16) == 1;
        }
        try {
            return Integer.parseInt(value) == 1;
        } catch (NumberFormatException e) {
            log.error("Failed to parse boolean value: {}", value, e);
        }
        return Boolean.parseBoolean(value);
    }
}
