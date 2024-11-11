package com.paicbd.smsc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsRecordsTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testUtilRecords() {
        UtilsRecords utilsRecords = new UtilsRecords();
        assertThat(UtilsRecords.class, BeanMatchers.hasValidGettersAndSetters());
        assertNotNull(utilsRecords);
    }

    @Test
    void testWebSocketConnectionParams() throws JsonProcessingException {
        UtilsRecords.WebSocketConnectionParams webSocketConnectionParams = new UtilsRecords.WebSocketConnectionParams(
            true,
            "localhost",
            8080,
            "/path",
            List.of("topic1", "topic2"),
            "key",
            "value",
            1000,
            "module"
        );
        assertEquals(true, webSocketConnectionParams.isWsEnabled());
        assertEquals("localhost", webSocketConnectionParams.host());
        assertEquals(8080, webSocketConnectionParams.port());
        assertEquals("/path", webSocketConnectionParams.path());
        assertEquals(List.of("topic1", "topic2"), webSocketConnectionParams.topicsToSubscribe());
        assertEquals("key", webSocketConnectionParams.headerKey());
        assertEquals("value", webSocketConnectionParams.headerValue());
        assertEquals(1000, webSocketConnectionParams.retryInterval());
        assertEquals("module", webSocketConnectionParams.module());
        assertEquals(objectMapper.writeValueAsString(webSocketConnectionParams), webSocketConnectionParams.toString());
    }

    @Test
    void testOptionalParameter() {
        UtilsRecords.OptionalParameter optionalParameter = new UtilsRecords.OptionalParameter(
            (short) 1,
            "value"
        );
        assertEquals(1, optionalParameter.tag());
        assertEquals("value", optionalParameter.value());
    }

    @Test
    void testSubmitSmResponseEvent() throws JsonProcessingException {
        UtilsRecords.SubmitSmResponseEvent submitSmResponseEvent = new UtilsRecords.SubmitSmResponseEvent(
            "hash-id",
            "1234",
            "systemId",
            "submitSmId",
            "submitSmServerId",
            "SMPP",
            1,
            "originNetworkType",
            "msgReferenceNumber",
            1,
            2, "1233456"
        );
        assertEquals("hash-id", submitSmResponseEvent.hashId());
        assertEquals("1234", submitSmResponseEvent.id());
        assertEquals("systemId", submitSmResponseEvent.systemId());
        assertEquals("submitSmId", submitSmResponseEvent.submitSmId());
        assertEquals("submitSmServerId", submitSmResponseEvent.submitSmServerId());
        assertEquals("SMPP", submitSmResponseEvent.originProtocol());
        assertEquals(1, submitSmResponseEvent.originNetworkId());
        assertEquals("originNetworkType", submitSmResponseEvent.originNetworkType());
        assertEquals("msgReferenceNumber", submitSmResponseEvent.msgReferenceNumber());
        assertEquals(1, submitSmResponseEvent.totalSegment());
        assertEquals(2, submitSmResponseEvent.segmentSequence());
        assertEquals(objectMapper.writeValueAsString(submitSmResponseEvent), submitSmResponseEvent.toString());
    }

    @Test
    void testJedisConfigParams() throws JsonProcessingException {
        UtilsRecords.JedisConfigParams jedisConfigParams = new UtilsRecords.JedisConfigParams(
            List.of("redisNode1", "redisNode2"),
            10,
            5,
            1,
            true
        );
        assertEquals(List.of("redisNode1", "redisNode2"), jedisConfigParams.redisNodes());
        assertEquals(10, jedisConfigParams.maxTotal());
        assertEquals(5, jedisConfigParams.maxIdle());
        assertEquals(1, jedisConfigParams.minIdle());
        assertTrue(jedisConfigParams.blockWhenExhausted());
        assertEquals(objectMapper.writeValueAsString(jedisConfigParams), jedisConfigParams.toString());
    }

    @Test
    void testCdrDetail() throws JsonProcessingException {
        UtilsRecords.CdrDetail cdrDetail = new UtilsRecords.CdrDetail(
                1L,
                "idEvent",
                "messageId",
                1,
                2,
                "originProtocol",
                "destProtocol",
                "originNetworkType",
                "destNetworkType",
                1,
                "module",
                "status",
                "comment",
                1,
                2,
                "sourceAddr",
                1,
                2,
                "destinationAddr",
                "imsi",
                "networkNodeNumber",
                "messageType",
                1,
                "errorCode",
                true,
                "cdrStatus",
                1L,
                2L,
                1,
                2,
                "sccpCalledPartyAddress",
                1,
                2,
                "sccpCallingPartyAddress",
                "globalTitle",
                "originatorSccpAddress",
                "udhi",
                "1",
                120,
                1,
                2,
                "message",
                "1",
                1,
                2,
                "parentId",
                123
        );
        assertEquals(objectMapper.writeValueAsString(cdrDetail), cdrDetail.toString());
    }

    @Test
    void testCdr() {
        assertDoesNotThrow(() -> new UtilsRecords.Cdr(
            "recordDate",
            "submitDate",
            "deliveryDate",
            "messageType",
            "messageId",
            "originationProtocol",
            "originationNetworkId",
            "originationType",
            "destinationProtocol",
            "destinationNetworkId",
            "destinationType",
            "routingId",
            "status",
            "statusCode",
            "comment",
            "dialogDuration",
            "processingTime",
            "dataCoding",
            "validityPeriod",
            "addrSrcDigits",
            "addrSrcTon",
            "addrSrcNpi",
            "addrDstDigits",
            "addrDstTon",
            "addrDstNpi",
            "remoteDialogId",
            "localDialogId",
            "localSpc",
            "localSsn",
            "localGlobalTitleDigits",
            "remoteSpc",
            "remoteSsn",
            "remoteGlobalTitleDigits",
            "imsi",
            "nnnDigits",
            "originatorSccpAddress",
            "mtServiceCenterAddress",
            "first20CharacterOfSms",
            "esmClass",
            "udhi",
            "registeredDelivery",
            "msgReferenceNumber",
            "totalSegment",
            "segmentSequence",
            "retryNumber",
            "parentId"
        ));
    }

    @Test
    void testCallbackHeaderHttp() {
        assertDoesNotThrow(() -> new UtilsRecords.CallbackHeaderHttp(
            "headerName",
            "headerValue"
        ));
    }

    @Test
    void testBroadcastStatistic() throws JsonProcessingException {
        var currentTimeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
        String dateTime = localDateTime.toString();
        var messageId = System.currentTimeMillis() + "-" + System.nanoTime();
        var broadcastStatistic = new UtilsRecords.BroadcastStatistic(
                1,
                messageId,
                1,
                dateTime
        );

        assertEquals(1, broadcastStatistic.broadcastId());
        assertEquals(messageId, broadcastStatistic.messageId());
        assertEquals(1, broadcastStatistic.status());
        assertEquals(dateTime, broadcastStatistic.date());
        String jsonResult = broadcastStatistic.toString();
        assertEquals(objectMapper.writeValueAsString(broadcastStatistic), jsonResult);
    }
}