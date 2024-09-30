package com.paicbd.smsc.utils;

import lombok.extern.slf4j.Slf4j;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataCodings;
import org.restcomm.protocols.ss7.map.datacoding.GSMCharset;
import org.restcomm.protocols.ss7.map.datacoding.GSMCharsetDecoder;
import org.restcomm.protocols.ss7.map.datacoding.GSMCharsetDecodingData;
import org.restcomm.protocols.ss7.map.datacoding.GSMCharsetEncoder;
import org.restcomm.protocols.ss7.map.datacoding.GSMCharsetEncodingData;
import org.restcomm.protocols.ss7.map.datacoding.Gsm7EncodingStyle;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Slf4j
public class SmppEncoding {
    public static final int GSM7 = 0;
    public static final int UTF8 = 1;
    public static final int UCS2 = 2;
    public static final int ISO88591 = 3;
    public static final int DCS_0 = 0;
    public static final int DCS_8 = 8;
    public static final int DCS_3 = 3;
    private static final Charset gsm7Charset = new GSMCharset("GSM", new String[]{});

    private SmppEncoding() {
        throw new IllegalStateException("Utility class");
    }

    public static DataCoding getDataCoding(int encodingType) {
        return switch (encodingType) {
            case DCS_0 -> DataCodings.newInstance((byte) 0); // GSM 7-bit
            case DCS_8 -> DataCodings.newInstance((byte) 0x08); // UCS-2
            case DCS_3 -> DataCodings.newInstance((byte) 0x03); // ISO-8859-1
            default -> throw new IllegalArgumentException("Unsupported encoding type: " + encodingType);
        };
    }

    public static byte[] encodeMessage(String message, int encodingType) {
        return switch (encodingType) {
            case GSM7 -> encodeGsm7(message);
            case UCS2 -> encodeUcs2(message);
            case ISO88591 -> encodeIso88591(message);
            case UTF8 -> encodeUtf8(message);
            default -> throw new IllegalStateException("Unexpected value: " + encodingType);
        };
    }

    public static String decodeMessage(byte[] messageBytes, int encodingType) {
        return switch (encodingType) {
            case GSM7 -> decodeGsm7(messageBytes);
            case UCS2 -> decodeUcs2(messageBytes);
            case ISO88591 -> decodeIso88591(messageBytes);
            case UTF8 -> decodeUtf8(messageBytes);
            default -> throw new IllegalStateException("Unexpected value: " + encodingType);
        };
    }

    private static byte[] encodeGsm7(String message) {
        GSMCharsetEncoder encoder = (GSMCharsetEncoder) gsm7Charset.newEncoder();
        encoder.setGSMCharsetEncodingData(new GSMCharsetEncodingData(Gsm7EncodingStyle.bit8_smpp_style, null));
        ByteBuffer byteBuffer = null;
        try {
            byteBuffer = encoder.encode(CharBuffer.wrap(message));
        } catch (CharacterCodingException e) {
            log.error("Error encoding GSM 7", e);
        }
        if (byteBuffer == null) {
            log.warn("GSM 7 encoding byteBuffer is null");
            return new byte[0];
        }
        byte[] data = new byte[byteBuffer.limit()];
        byteBuffer.get(data);
        return data;
    }

    private static byte[] encodeUcs2(String message) {
        return message.getBytes(StandardCharsets.UTF_16BE);
    }

    private static byte[] encodeIso88591(String message) {
        return message.getBytes(StandardCharsets.ISO_8859_1);
    }

    private static byte[] encodeUtf8(String message) {
        return message.getBytes(StandardCharsets.UTF_8);
    }

    private static String decodeGsm7(byte[] messageBytes) {
        GSMCharsetDecoder decoder = (GSMCharsetDecoder) gsm7Charset.newDecoder();
        decoder.setGSMCharsetDecodingData(new GSMCharsetDecodingData(Gsm7EncodingStyle.bit8_smpp_style,
                Integer.MAX_VALUE, 0));
        ByteBuffer byteBuffer = ByteBuffer.wrap(messageBytes);
        CharBuffer charBuffer = null;
        try {
            charBuffer = decoder.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            log.error("Error decoding message", e);
        }
        if (charBuffer == null) {
            log.warn("GSM 7 decoding charBuffer is null");
            return null;
        }
        return charBuffer.toString();
    }

    private static String decodeUcs2(byte[] messageBytes) {
        return new String(messageBytes, StandardCharsets.UTF_16BE);
    }

    private static String decodeIso88591(byte[] messageBytes) {
        return new String(messageBytes, StandardCharsets.ISO_8859_1);
    }

    private static String decodeUtf8(byte[] messageBytes) {
        return new String(messageBytes, StandardCharsets.UTF_8);
    }

    public static boolean isValidDataCoding(Integer code) {
        int validCode = Optional.ofNullable(code).orElse(0);
        return validCode == DCS_0 || validCode == DCS_3 || validCode == DCS_8;
    }
}
