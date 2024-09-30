package com.paicbd.smsc.utils;

import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataCodings;
import org.junit.jupiter.api.Test;

import static com.paicbd.smsc.utils.ConverterTest.testPrivateConstructor;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmppEncodingTest {
    @Test
    void testSmppEncodingTestPrivateConstructor() throws NoSuchMethodException {
        testPrivateConstructor(SmppEncoding.class);
    }

    @Test
    void getDataCodingTest() {
        DataCoding dc0 = SmppEncoding.getDataCoding(0);
        DataCoding dc3 = SmppEncoding.getDataCoding(3);
        DataCoding dc8 = SmppEncoding.getDataCoding(8);

        assertEquals(DataCodings.newInstance((byte) 0), dc0);
        assertEquals(DataCodings.newInstance((byte) 3), dc3);
        assertEquals(DataCodings.newInstance((byte) 8), dc8);

        assertThrows(IllegalArgumentException.class, () -> SmppEncoding.getDataCoding(10));
    }

    @Test
    void encodeAndDecodeMessageTest() {
        String message = "Hello, World!";
        byte[] gsm7 = SmppEncoding.encodeMessage(message, 0);
        byte[] ucs2 = SmppEncoding.encodeMessage(message, 2);
        byte[] iso88591 = SmppEncoding.encodeMessage(message, 3);
        byte[] utf8 = SmppEncoding.encodeMessage(message, 1);

        assertEquals("Hello, World!", SmppEncoding.decodeMessage(gsm7, 0));
        assertEquals("Hello, World!", SmppEncoding.decodeMessage(ucs2, 2));
        assertEquals("Hello, World!", SmppEncoding.decodeMessage(iso88591, 3));
        assertEquals("Hello, World!", SmppEncoding.decodeMessage(utf8, 1));

        assertThrows(IllegalStateException.class, () -> SmppEncoding.encodeMessage(message, 10));
        assertThrows(IllegalStateException.class, () -> SmppEncoding.decodeMessage(utf8, 10));
    }

    @Test
    void isValidDataCodingTest() {
        assertDoesNotThrow(() -> SmppEncoding.isValidDataCoding(0));
        assertDoesNotThrow(() -> SmppEncoding.isValidDataCoding(3));
        assertDoesNotThrow(() -> SmppEncoding.isValidDataCoding(8));
        assertDoesNotThrow(() -> SmppEncoding.isValidDataCoding(100));
        assertFalse(SmppEncoding.isValidDataCoding(10));
        assertTrue(SmppEncoding.isValidDataCoding(0));
    }
}