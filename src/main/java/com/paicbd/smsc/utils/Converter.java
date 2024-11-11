package com.paicbd.smsc.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paicbd.smsc.dto.UtilsRecords;
import com.paicbd.smsc.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jsmpp.bean.OptionalParameter;
import redis.clients.jedis.Connection;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class Converter {
    private static final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final String MESSAGE = "message";

    private Converter() {
        throw new IllegalStateException("Utility class");
    }

    public static JedisCluster paramsToJedisCluster(UtilsRecords.JedisConfigParams params) {
        try {
            Set<HostAndPort> jedisClusterNodes = new HashSet<>();
            params.redisNodes().forEach(node -> {
                String[] nodePart = node.split(":");
                jedisClusterNodes.add(new HostAndPort(nodePart[0], Integer.parseInt(nodePart[1])));
            });

            GenericObjectPoolConfig<Connection> poolConfig = new GenericObjectPoolConfig<>();
            poolConfig.setMaxTotal(params.maxTotal());
            poolConfig.setMinIdle(params.minIdle());
            poolConfig.setMaxIdle(params.maxIdle());
            poolConfig.setBlockWhenExhausted(params.blockWhenExhausted());
            var jc = new JedisCluster(jedisClusterNodes, 2000, 2000, 20, poolConfig);
            log.info("JedisCluster instance was created successfully with params: {}", params);
            return jc;
        } catch (Exception e) {
            log.error("An error occurred while creating JedisCluster instance: {}", e.getMessage());
            return null;
        }
    }

    public static String valueAsString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while converting object to string: {}", e.getMessage());
            throw new RTException("An error occurred while converting object to string", e);
        }
    }

    /**
     * Converts a CdrDetail object into a JSON string representing a Cdr object, ensuring there are no null values.
     *
     * @param cdrDetail The CdrDetail object to convert.
     * @return A JSON string representing the Cdr object with the corresponding values from cdrDetail.
     */
    public static String convertCdrDetailToCdrJson(UtilsRecords.CdrDetail cdrDetail) {
        long generationDate = System.currentTimeMillis();
        long submitDate = parseSubmitDate(cdrDetail.idEvent()); // 1717535359756
        String timestampStr = String.valueOf(cdrDetail.timestamp());
        long dialogDuration = cdrDetail.timestamp() - submitDate;
        long processingTime = generationDate - submitDate;

        UtilsRecords.Cdr cdr = new UtilsRecords.Cdr(
                defaultIfNull(String.valueOf(generationDate)),
                defaultIfNull(Long.toString(submitDate)),
                defaultIfNull(timestampStr),
                defaultIfNull(cdrDetail.messageType()),
                defaultIfNull(cdrDetail.messageId()),
                defaultIfNull(cdrDetail.originProtocol()),
                defaultIfNull(cdrDetail.originNetworkId()),
                defaultIfNull(cdrDetail.originNetworkType()),
                defaultIfNull(cdrDetail.destProtocol()),
                defaultIfNull(cdrDetail.destNetworkId()),
                defaultIfNull(cdrDetail.destNetworkType()),
                defaultIfNull(Integer.toString(cdrDetail.routingId())),
                defaultIfNull(cdrDetail.status()),
                defaultIfNull(cdrDetail.cdrStatus()),
                defaultIfNull(cdrDetail.comment()),
                defaultIfNull(Long.toString(dialogDuration)),
                defaultIfNull(Long.toString(processingTime)),
                defaultIfNull(cdrDetail.dataCoding()),
                defaultIfNull(cdrDetail.validityPeriod()),
                defaultIfNull(cdrDetail.sourceAddr()),
                defaultIfNull(cdrDetail.sourceAddrTon()),
                defaultIfNull(cdrDetail.sourceAddrNpi()),
                defaultIfNull(cdrDetail.destinationAddr()),
                defaultIfNull(cdrDetail.destAddrTon()),
                defaultIfNull(cdrDetail.destAddrNpi()),
                defaultIfNull(cdrDetail.remoteDialogId()),
                defaultIfNull(cdrDetail.localDialogId()),
                defaultIfNull(cdrDetail.sccpCallingPartyAddressPointCode()),
                defaultIfNull(cdrDetail.sccpCallingPartyAddressSubSystemNumber()),
                defaultIfNull(cdrDetail.sccpCallingPartyAddress()),
                defaultIfNull(cdrDetail.sccpCalledPartyAddressPointCode()),
                defaultIfNull(cdrDetail.sccpCalledPartyAddressSubSystemNumber()),
                defaultIfNull(cdrDetail.sccpCalledPartyAddress()),
                defaultIfNull(cdrDetail.imsi()),
                defaultIfNull(cdrDetail.networkNodeNumber()),
                defaultIfNull(cdrDetail.originatorSccpAddress()),
                defaultIfNull(cdrDetail.globalTitle()),
                defaultIfNull(cdrDetail.message()),
                defaultIfNull(cdrDetail.esmClass()),
                defaultIfNull(cdrDetail.udhi()),
                defaultIfNull(cdrDetail.registeredDelivery()),
                defaultIfNull(cdrDetail.msgReferenceNumber()),
                defaultIfNull(cdrDetail.totalSegment()),
                defaultIfNull(cdrDetail.segmentSequence()),
                defaultIfNull(cdrDetail.retryNumber()),
                defaultIfNull(cdrDetail.parentId())
        );

        return valueAsString(cdr);
    }

    /**
     * Deserializes a JSON string into a CdrDetail object.
     *
     * @param cdrDetailJson The JSON string representing the CdrDetail.
     * @return A CdrDetail object with the corresponding values from the JSON string.
     * @throws RTException If an error occurs while deserializing the JSON string.
     */
    public static UtilsRecords.CdrDetail deserializeCdrDetail(String cdrDetailJson) {
        try {
            return mapper.readValue(cdrDetailJson, UtilsRecords.CdrDetail.class);
        } catch (JsonProcessingException e) {
            log.error("An error occurred while deserializing CdrDetail JSON: {}", e.getMessage());
            throw new RTException("An error occurred while deserializing CdrDetail JSON", e);
        }
    }

    public static Long parseSubmitDate(String idEvent) {
        return Long.parseLong(idEvent.split("-")[0]);
    }

    private static String defaultIfNull(Object value) {
        return value == null ? "" : value.toString();
    }

    public static Map<String, Object> bytesToUdhMap(byte[] message) {
        Map<String, Object> udhMap = new HashMap<>();
        int index = 0;
        int udhLength = Byte.toUnsignedInt(message[index]);
        index += 1;

        int udhIndex = index;
        while (udhIndex < index + udhLength) {
            int headerId = Byte.toUnsignedInt(message[udhIndex]);
            udhIndex += 1;
            int elementCount = Byte.toUnsignedInt(message[udhIndex]);
            udhIndex += 1;

            int[] elements = new int[elementCount];
            for (int i = 0; i < elementCount; i++) {
                elements[i] = Byte.toUnsignedInt(message[udhIndex + i]);
            }
            udhMap.put(String.format("0x%02X", headerId), elements);
            udhIndex += elementCount;
        }

        byte[] currentMessageBytes = Arrays.copyOfRange(message, udhIndex, message.length);
        String currentMessage = new String(currentMessageBytes);

        udhMap.put(MESSAGE, currentMessage);
        return udhMap;
    }

    public static byte[] jsonToUdhBytes(Map<String, Object> udhMap) {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte) 0);

        for (Map.Entry<String, Object> entry : udhMap.entrySet()) {
            if (!entry.getKey().equals(MESSAGE)) {
                int headerId = Integer.parseInt(entry.getKey().substring(2), 16);
                Object value = entry.getValue();
                if (value instanceof int[] elements) {
                    byteList.add((byte) headerId);
                    byteList.add((byte) elements.length);
                    for (int element : elements) {
                        byteList.add((byte) element);
                    }
                } else {
                    throw new IllegalArgumentException("Expected an array of integers for key: " + entry.getKey());
                }
            }
        }

        byteList.set(0, (byte) (byteList.size() - 1));
        String message = (String) udhMap.get(MESSAGE);
        byte[] messageBytes = message.getBytes();
        for (byte b : messageBytes) {
            byteList.add(b);
        }

        byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }

        return byteArray;
    }

    public static byte[] paramsToUdhBytes(String message, int identifier, int parts, int partNumber) {
        byte[] messageBytes = message.getBytes();
        byte[] udh = new byte[6];
        udh[0] = 5;
        udh[1] = 0;
        udh[2] = 3;
        udh[3] = (byte) identifier;
        udh[4] = (byte) parts;
        udh[5] = (byte) partNumber;
        byte[] result = new byte[messageBytes.length + udh.length];
        System.arraycopy(udh, 0, result, 0, udh.length);
        System.arraycopy(messageBytes, 0, result, udh.length, messageBytes.length);
        return result;
    }

    public static Map<String, Object> jsonToUdhMap(String udhJson) {
        try {
            return mapper.readValue(udhJson, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("An error occurred while deserializing UDH JSON: {}", e.getMessage());
            throw new RTException("An error occurred while deserializing UDH JSON", e);
        }
    }

    public static <T> T stringToObject(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (RuntimeException | JsonProcessingException e) {
            log.error("An error occurred while deserializing Object: {}", e.getMessage(), e);
            throw new RTException("An error occurred while deserializing Object", e);
        }
    }

    public static <T> T stringToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("An error occurred while deserializing Object returning NULL", e);
            return null;
        }
    }

    public static OptionalParameter[] convertToOptionalParameters(int identifier, int parts, int partNumber) {
        List<OptionalParameter> optionalParameters = new ArrayList<>();
        optionalParameters.add(new OptionalParameter.Sar_msg_ref_num((byte) identifier));
        optionalParameters.add(new OptionalParameter.Sar_total_segments((byte) parts));
        optionalParameters.add(new OptionalParameter.Sar_segment_seqnum((byte) partNumber));
        return optionalParameters.toArray(new OptionalParameter[0]);
    }

    public static byte[] jsonStringToBytes(String jsonString) {
        Map<String, Object> map = jsonToUdhMap(jsonString);
        return jsonToUdhBytes(map);
    }

    public static String udhMapToJson(Map<String, Object> udh) {
        return valueAsString(udh);
    }

    public static boolean hasValidValue(String value) {
        return value != null && !value.isEmpty();
    }

    public static byte[] paramsToUdhBytes(Map<String, Object> udhMap, int encodingType, boolean includeMessage) {
        List<Byte> byteList = new ArrayList<>();

        for (Map.Entry<String, Object> entry : udhMap.entrySet()) {
            String key = entry.getKey();
            if (!key.equals(MESSAGE)) {
                List<Integer> values = (List<Integer>) (entry.getValue());
                byte[] udhBytes = udhToBytes(key, values);
                for (byte b : udhBytes) {
                    byteList.add(b);
                }
            }
        }

        // count only udh headers
        int totalLength = byteList.size();
        byteList.addFirst((byte) totalLength);

        // message bytes is not necessary for SS7
        if (Boolean.TRUE.equals(includeMessage) && udhMap.containsKey(MESSAGE)) {
            String message = (String) udhMap.get(MESSAGE);
            byte[] messageBytes = SmppEncoding.encodeMessage(message, encodingType);
            for (byte b : messageBytes) {
                byteList.add(b);
            }
        }

        byte[] udhArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            udhArray[i] = byteList.get(i);
        }

        return udhArray;
    }

    private static byte[] udhToBytes(String key, List<Integer> values) {
        int keyValue = Integer.parseInt(key.substring(2), 16);
        byte[] byteArray = new byte[2 + values.size()];

        byteArray[0] = (byte) keyValue;
        byteArray[1] = (byte) values.size();
        for (int i = 0; i < values.size(); i++) {
            byteArray[2 + i] = values.get(i).byteValue();
        }

        return byteArray;
    }

    public static String secondsToRelativeValidityPeriod(long totalSeconds) {
        if (totalSeconds < 0) {
            throw new IllegalArgumentException("Total seconds cannot be negative.");
        }

        long years = totalSeconds / (365L * 24 * 60 * 60);
        totalSeconds %= 365L * 24 * 60 * 60;

        long months = totalSeconds / (30L * 24 * 60 * 60);
        totalSeconds %= 30L * 24 * 60 * 60;

        long days = totalSeconds / (24 * 60 * 60);
        totalSeconds %= 24 * 60 * 60;

        long hours = totalSeconds / (60 * 60);
        totalSeconds %= 60 * 60;

        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String formattedValues = String.format("%02d%02d%02d%02d%02d%02d000R",
                years, months, days, hours, minutes, seconds);
        log.debug("Formatted values: {}", formattedValues);
        return formattedValues;
    }

    public static long smppValidityPeriodToSeconds(String smppTime) {
        if (smppTime == null || smppTime.length() != 16) {
            throw new IllegalArgumentException("Invalid SMPP time format.");
        }

        char lastChar = smppTime.charAt(15);

        if (lastChar == 'R') {
            return parseRelativeTimeToSeconds(smppTime);
        }

        if (lastChar == '+' || lastChar == '-') {
            return parseAbsoluteTimeToSeconds(smppTime);
        }

        throw new IllegalArgumentException("Unsupported SMPP time format: " + smppTime);
    }

    private static long parseRelativeTimeToSeconds(String smppTime) {
        DateTimeRecord dateTimeRecord = createDateTimeMap(smppTime, false);
        long totalSeconds = (dateTimeRecord.year() * 365L * 24 * 60 * 60) +
                            (dateTimeRecord.month() * 30L * 24 * 60 * 60) +
                            (dateTimeRecord.day() * 24L * 60 * 60) +
                            (dateTimeRecord.hour() * 60L * 60) +
                            (dateTimeRecord.minute() * 60L) +
                            dateTimeRecord.second();
        log.debug("SMPP validity period: {} converted to seconds: {}", smppTime, totalSeconds);
        return totalSeconds;
    }

    public static long parseAbsoluteTimeToSeconds(String absoluteTime) {
        DateTimeRecord dateTimeRecord = createDateTimeMap(absoluteTime, true);
        int quarterHourOffset = Integer.parseInt(absoluteTime.substring(13, 15));
        char timezoneSign = absoluteTime.charAt(15);  // '+' o '-'
        LocalDateTime parsedDateTime = getLocalDateTime(dateTimeRecord, quarterHourOffset, timezoneSign);

        LocalDateTime currentTime = LocalDateTime.now();
        Duration durationDifference = Duration.between(currentTime, parsedDateTime);
        return durationDifference.getSeconds();
    }

    private static LocalDateTime getLocalDateTime(DateTimeRecord dateTimeRecord, int quarterHourOffset, char timezoneSign) {
        LocalDateTime parsedDateTime = LocalDateTime.of(
                dateTimeRecord.year(),
                dateTimeRecord.month(),
                dateTimeRecord.day(),
                dateTimeRecord.hour(),
                dateTimeRecord.minute(),
                dateTimeRecord.second()
        );

        int offsetInMinutes = quarterHourOffset * 15;
        if (timezoneSign == '+') {
            parsedDateTime = parsedDateTime.plusMinutes(offsetInMinutes);
        } else {
            parsedDateTime = parsedDateTime.minusMinutes(offsetInMinutes);
        }
        return parsedDateTime;
    }

    public static DateTimeRecord createDateTimeMap(String dateTime, boolean addMile) {
        int year = Integer.parseInt(dateTime.substring(0, 2));
        year += addMile ? 2000 : 0;
        int month = Integer.parseInt(dateTime.substring(2, 4));
        int day = Integer.parseInt(dateTime.substring(4, 6));
        int hour = Integer.parseInt(dateTime.substring(6, 8));
        int minute = Integer.parseInt(dateTime.substring(8, 10));
        int second = Integer.parseInt(dateTime.substring(10, 12));
        return new DateTimeRecord(year, month, day, hour, minute, second);
    }

    public record DateTimeRecord(
            int year,
            int month,
            int day,
            int hour,
            int minute,
            int second
    ) {
    }
}
