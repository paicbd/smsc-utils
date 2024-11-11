package com.paicbd.smsc.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.paicbd.smsc.dto.GeneralSettings;
import com.paicbd.smsc.dto.UtilsRecords;
import com.paicbd.smsc.exception.RTException;
import org.jsmpp.bean.OptionalParameter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConverterTest {
    static final String MESSAGE = "message";

    @Test
    void testConstantsPrivateConstructor() throws NoSuchMethodException {
        testPrivateConstructor(Converter.class);
    }

    @Test
    void paramsToJedisCluster() {
        UtilsRecords.JedisConfigParams jedisConfigParams = new UtilsRecords.JedisConfigParams(
            List.of("localhost:7000", "localhost:7001", "localhost:7002", "localhost:7003", "localhost:7004", "localhost:7005", "localhost:7006", "localhost:7007", "localhost:7008", "localhost:7009"),
            1000,
            1000,
            1000,
            true);
        assertNotNull(jedisConfigParams);
        assertDoesNotThrow(() -> Converter.paramsToJedisCluster(jedisConfigParams));
    }

    @Test
    void testValueAsString() {
        Map<String, Object> map = new HashMap<>(Map.of("key1", "value1", "key2", "value2"));
        assertDoesNotThrow(() -> Converter.valueAsString(map));

        map.put("key3", new Object());
        assertThrows(RTException.class, () -> Converter.valueAsString(map));
    }

    @Test
    void testConvertCdrDetailToCdrJson() {
        UtilsRecords.CdrDetail cdrDetail = new UtilsRecords.CdrDetail(
                1L,
                "1717535359756",
                "12345",
                1,
                2,
                "SMPP",
                "HTTP",
                "SP",
                "GW",
                1,
                "SMPP_CLIENT",
                "DELIVERD",
                "NOTHING",
                1,
                2,
                "1234",
                1,
                2,
                "4567",
                "imsi",
                null,
                "MESSAGE",
                0,
                null,
                true,
                "ENQUEUE",
                1L,
                2L,
                1,
                2,
                null,
                1,
                2,
                null,
                "011",
                null,
                "{}",
                "3",
                120,
                1,
                3,
                    "TEST MESSAGE",
                "1",
                1,
                2,
                null,
                123
        );
        assertDoesNotThrow(() -> Converter.convertCdrDetailToCdrJson(cdrDetail));
    }

    @Test
    void testDeserializeCdrDetail() {
        String cdrDetailJson = "{\"timestamp\":1719327181964,\"id_event\":\"1719327141615-2966989339944\",\"message_id\":\"1719327141615-2966989339944\",\"origin_network_id\":1,\"dest_network_id\":2,\"origin_protocol\":\"HTTP\",\"dest_protocol\":\"SS7\",\"origin_network_type\":\"SP\",\"dest_network_type\":\"GW\",\"routing_id\":1,\"module\":\"SS7_CLIENT\",\"status\":null,\"comment\":\"\",\"source_addr_ton\":4,\"source_addr_npi\":1,\"source_addr\":\"50510201020\",\"dest_addr_ton\":4,\"dest_addr_npi\":1,\"destination_addr\":\"50582368999\",\"imsi\":null,\"network_node_number\":null,\"message_type\":\"MESSAGE\",\"retry_number\":null,\"error_code\":null,\"is_dlr\":false,\"cdr_status\":\"SENT\",\"remote_dialog_id\":null,\"local_dialog_dd\":14,\"sccp_called_party_address_point_code\":0,\"sccp_called_party_address_sub_system_number\":6,\"sccp_called_party_address\":\"50582368999\",\"sccp_calling_party_address_point_code\":0,\"sccp_calling_party_address_sub_system_number\":8,\"sccp_calling_party_address\":\"22220\",\"global_title\":\"22220\",\"originator_sccp_address\":null,\"udhi\":null,\"esm_class\":\"67\",\"validity_period\":\"60\",\"registered_delivery\":1,\"data_coding\":0,\"message\":\"Java es un lenguaje \",\"msg_reference_number\":null,\"total_segment\":null,\"segment_sequence\":null,\"parent_id\":\"1719327141615-2966989339944\"}";
        assertDoesNotThrow(() -> Converter.deserializeCdrDetail(cdrDetailJson));
        String invalidJson = "{jsad:fzas}";
        assertThrows(RTException.class, () -> Converter.deserializeCdrDetail(invalidJson));
    }

    @Test
    void testBytesToUdhMap() {
        byte[] udh = new byte[]{0x05, 0x00, 0x03, 0x0A, 0x01, 0x02};
        assertDoesNotThrow(() -> Converter.bytesToUdhMap(udh));

        byte[] invalidUdh = new byte[]{0x05, 0x00, 0x03, 0x01, 0x02};
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Converter.bytesToUdhMap(invalidUdh));

        // return valid Map
        byte[] udh2 = new byte[]{0x05, 0x00, 0x03, 0x0A, 0x01, 0x02, 0x05, 0x00, 0x03, 0x0A, 0x01, 0x02};
        var map = Converter.bytesToUdhMap(udh2);
        assertEquals(2, map.size());
    }

    @Test
    void testJsonToUdhBytes() {
        Map<String, Object> udhMap = new HashMap<>();
        udhMap.put("0x00", new int[]{0x03, 0x01, 0x01, 0x02});
        udhMap.put(MESSAGE, "message");
        assertDoesNotThrow(() -> Converter.jsonToUdhBytes(udhMap));

        Map<String, Object> invalidUdhMap = new HashMap<>();
        invalidUdhMap.put("0x00", new char[]{0x03, 1101, 0x01, 0x02});
        invalidUdhMap.put(MESSAGE, "message");
        assertThrows(IllegalArgumentException.class, () -> Converter.jsonToUdhBytes(invalidUdhMap));
    }

    @Test
    void testParamsToUdhBytes() {
        String message = "hello";
        int identifier = 10;
        int parts = 4;
        int partNumber = 2;
        assertDoesNotThrow(() -> Converter.paramsToUdhBytes(message, identifier, parts, partNumber));
        assertInstanceOf(byte[].class, Converter.paramsToUdhBytes(message, identifier, parts, partNumber));
    }

    @Test
    void testJsonToUdhMap() {
        String json = "{\"0x00\":[3,1,1,2],\"message\":\"message\"}";
        assertDoesNotThrow(() -> Converter.jsonToUdhMap(json));
        String invalidJson = "{\"0x00\":[3,1,1,2],\"message\":\"message\"";
        assertThrows(RTException.class, () -> Converter.jsonToUdhMap(invalidJson));
    }

    @Test
    void testCastParamsToOptionalParams() {
        int identifier = 11;
        int parts = 2;
        int partNumber = 1;
        assertDoesNotThrow(() -> Converter.convertToOptionalParameters(identifier, parts, partNumber));
        assertInstanceOf(OptionalParameter[].class, Converter.convertToOptionalParameters(identifier, parts, partNumber));
    }

    @Test
    void testHasValidValue() {
        String value = "value";
        assertTrue(Converter.hasValidValue(value));

        String emptyValue = "";
        assertFalse(Converter.hasValidValue(emptyValue));
    }

    @Test
    void testParamsToUdhBytes_otherParams() {
        Map<String, Object> udhMap = new HashMap<>();
        udhMap.put("0x00", List.of(3, 1, 1, 2));
        udhMap.put(MESSAGE, "message");
        int encodingType = 0;
        boolean includeMessage = true;
        assertDoesNotThrow(() -> Converter.paramsToUdhBytes(udhMap, encodingType, includeMessage));
        assertInstanceOf(byte[].class, Converter.paramsToUdhBytes(udhMap, encodingType, includeMessage));

        udhMap.remove(MESSAGE);
        assertDoesNotThrow(() -> Converter.paramsToUdhBytes(udhMap, encodingType, false));
    }

    public static <T> void testPrivateConstructor(Class<T> clazz) throws NoSuchMethodException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }


    @Test
    void testConvertToObject() {
        String generalSettingInRaw = "{\"id\":1,\"validity_period\":60,\"max_validity_period\":240,\"source_addr_ton\":1,\"source_addr_npi\":1,\"dest_addr_ton\":1,\"dest_addr_npi\":1,\"encoding_iso88591\":3,\"encoding_gsm7\":0,\"encoding_ucs2\":2}";
        String generalSettingInRawWithError = "{\"id\":1,\"validity_period\":60,\"max_validity_period\":240,\"source_addr_ton\":1,\"source_addr_npi\":1,\"dest_addr_ton\":1,\"dest_addr_npi\":1,\"encoding_iso88591\":3,}";

        GeneralSettings generalSetting;

        generalSetting = Converter.stringToObject(generalSettingInRaw, new TypeReference<GeneralSettings>() {
        });
        assertNotNull(generalSetting);
        assertEquals(1, generalSetting.getId());
        assertEquals(60, generalSetting.getValidityPeriod());
        assertThrows(Exception.class, () -> Converter.stringToObject(generalSettingInRawWithError, new TypeReference<GeneralSettings>() {}));
        generalSetting = Converter.stringToObject(generalSettingInRaw, GeneralSettings.class);
        assertNotNull(generalSetting);
        assertEquals(1, generalSetting.getId());
        assertEquals(60, generalSetting.getValidityPeriod());
        assertNull(Converter.stringToObject(generalSettingInRawWithError, GeneralSettings.class));
    }

    @Test
    void testSecondsToRelativeValidityPeriod() {
        assertEquals("000000000010000R", Converter.secondsToRelativeValidityPeriod(10)); // 10 seconds
        assertEquals("000000020000000R", Converter.secondsToRelativeValidityPeriod(7200)); // 2 hours
        assertEquals("000001000000000R", Converter.secondsToRelativeValidityPeriod(86400)); // 1 day

        assertThrows(IllegalArgumentException.class, () -> Converter.secondsToRelativeValidityPeriod(-10));
    }

    @Test
    void testSmppValidityPeriodToSeconds() {
        // illegal values
        assertThrows(IllegalArgumentException.class, () -> Converter.smppValidityPeriodToSeconds("000000000010000R8"));

        assertEquals(10, Converter.smppValidityPeriodToSeconds("000000000010000R"));
        assertEquals(7200, Converter.smppValidityPeriodToSeconds("000000020000000R"));
        assertEquals(86400, Converter.smppValidityPeriodToSeconds("000001000000000R"));

        LocalDateTime dt = LocalDateTime.now();
        dt = dt.plusHours(2);
        int currentYear = dt.getYear();
        currentYear -= 2000;
        int currentMonth = dt.getMonthValue();
        int currentDay = dt.getDayOfMonth();
        int currentHour = dt.getHour();
        int currentMinute = dt.getMinute();
        int currentSecond = dt.getSecond();

        String absoluteTime = String.format("%02d%02d%02d%02d%02d%02d000+", currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond);
        assertTrue(Converter.smppValidityPeriodToSeconds(absoluteTime) > 0);

        absoluteTime = String.format("%02d%02d%02d%02d%02d%02d000-", currentYear, currentMonth, currentDay, currentHour, currentMinute, currentSecond);
        assertTrue(Converter.smppValidityPeriodToSeconds(absoluteTime) > 0);

        // illegal last character
        assertThrows(IllegalArgumentException.class, () -> Converter.smppValidityPeriodToSeconds("000000020000000X"));

        // illegal because null
        assertThrows(IllegalArgumentException.class, () -> Converter.smppValidityPeriodToSeconds(null));
    }
}