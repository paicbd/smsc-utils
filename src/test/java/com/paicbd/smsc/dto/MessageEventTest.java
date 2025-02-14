package com.paicbd.smsc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import com.paicbd.smsc.utils.UtilsEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MessageEventTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        assertThat(MessageEvent.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testToString() throws JsonProcessingException {
        MessageEvent messageEvent = new MessageEvent();
        Assertions.assertEquals(messageEvent.toString(), objectMapper.writeValueAsString(messageEvent));
    }

    @Test
    void testMessageEventToCdrDetail() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setSystemId("systemId");
        messageEvent.setMessageId("messageId");
        messageEvent.setEsmClass(3);
        UtilsRecords.CdrDetail cdrDetail = messageEvent.toCdrDetail(UtilsEnum.Module.SMPP_CLIENT, UtilsEnum.MessageType.MESSAGE,
                UtilsEnum.CdrStatus.RECEIVED, "");
        Assertions.assertEquals("messageId", cdrDetail.messageId());

        messageEvent.setShortMessage("This message is too long to be displayed");
        assertDoesNotThrow(() -> messageEvent.toCdrDetail(UtilsEnum.Module.SMPP_CLIENT, UtilsEnum.MessageType.MESSAGE,
                UtilsEnum.CdrStatus.RECEIVED, ""));

        messageEvent.setShortMessage("This message is");
        assertDoesNotThrow(() -> messageEvent.toCdrDetail(UtilsEnum.Module.SMPP_CLIENT, UtilsEnum.MessageType.MESSAGE,
                UtilsEnum.CdrStatus.RECEIVED, ""));

        messageEvent.setGlobalTitle("globalTitle");
        messageEvent.setOriginatorSccpAddress("originatorSccpAddress");
        messageEvent.setUdhi("{}");
        messageEvent.setEsmClass(null);
        messageEvent.setRegisteredDelivery(0);
        messageEvent.setValidityPeriod(120);
        messageEvent.setDataCoding(0);
        assertDoesNotThrow(() -> messageEvent.toCdrDetail(UtilsEnum.Module.SMPP_CLIENT, UtilsEnum.MessageType.MESSAGE,
                UtilsEnum.CdrStatus.RECEIVED, ""));

        messageEvent.setDataCoding(8);
        assertDoesNotThrow(() -> messageEvent.toCdrDetail(UtilsEnum.Module.SMPP_CLIENT, UtilsEnum.MessageType.MESSAGE,
                UtilsEnum.CdrStatus.RECEIVED, ""));
    }

    @Test
    void testTransformToMtDeliver() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setSystemId("systemId");
        messageEvent.setMessageId("messageId");
        messageEvent.setSourceAddr("123");
        messageEvent.setDestinationAddr("324");
        messageEvent.setShortMessage("shortMessage");
        messageEvent.setSourceAddrTon(1);
        messageEvent.setSourceAddrNpi(1);
        messageEvent.setDestAddrTon(1);
        messageEvent.setDestAddrNpi(1);
        assertDoesNotThrow(messageEvent::transformToSs7MtDeliver);
    }

    @Test
    void testObtainCustomParentId() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setShortMessage("id:123121224234234 ");
        messageEvent.setSegmentSequence(1);
        messageEvent.setTotalSegment(2);
        Assertions.assertEquals("123121224234234", messageEvent.obtainCustomParentId());

        messageEvent.setTotalSegment(1);
        Assertions.assertNull(messageEvent.obtainCustomParentId());

        messageEvent.setShortMessage("NOT ID");
        Assertions.assertNull(messageEvent.obtainCustomParentId());
    }

    @Test
    void testNotApplyForLongMessage() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setOriginProtocol("HTTP");
        messageEvent.setOriginNetworkType("SP");
        messageEvent.setDestProtocol("HTTP");
        messageEvent.setDestNetworkType("GW");
        Assertions.assertTrue(messageEvent.notApplyForLongMessage());

        messageEvent.setOriginProtocol("SMPP");
        messageEvent.setOriginNetworkType("SP");
        messageEvent.setDestProtocol("HTTP");
        messageEvent.setDestNetworkType("GW");
        Assertions.assertTrue(messageEvent.notApplyForLongMessage());


        messageEvent.setOriginNetworkType("SP");
        messageEvent.setOriginProtocol("HTTP");
        messageEvent.setDestNetworkType("GW");
        messageEvent.setDestProtocol("SMPP");
        Assertions.assertFalse(messageEvent.notApplyForLongMessage());
    }

    @Test
    void testApplyForLongMessage() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setOriginProtocol("HTTP");
        messageEvent.setOriginNetworkType("SP");
        messageEvent.setDestProtocol("HTTP");
        messageEvent.setDestNetworkType("GW");
        Assertions.assertFalse(messageEvent.applyForLongMessage());

        messageEvent.setOriginProtocol("HTTP");
        messageEvent.setOriginNetworkType("SP");
        messageEvent.setDestProtocol("SMPP");
        messageEvent.setDestNetworkType("GW");
        Assertions.assertTrue(messageEvent.applyForLongMessage());

        messageEvent.setOriginNetworkType("SP");
        messageEvent.setOriginProtocol("SMPP");
        messageEvent.setDestNetworkType("SP");
        messageEvent.setDestProtocol("HTTP");
        Assertions.assertFalse(messageEvent.applyForLongMessage());
    }

    @Test
    void testClone() {
        MessageEvent messageEvent = new MessageEvent();
        messageEvent.setId("id");
        messageEvent.setSystemId("systemId");
        messageEvent.setMessageId("messageId");
        messageEvent.setSourceAddr("123");
        messageEvent.setDestinationAddr("324");
        messageEvent.setShortMessage("shortMessage");
        messageEvent.setSourceAddrTon(1);
        messageEvent.setSourceAddrNpi(1);
        messageEvent.setDestAddrTon(1);
        messageEvent.setDestAddrNpi(1);
        messageEvent.setBroadcastId(1234);

        MessageEvent messageEvent1 = new MessageEvent();
        messageEvent1.setId("id1");
        messageEvent1.setSystemId("systemId1");
        messageEvent1.setMessageId("messageId1");
        messageEvent1.setSourceAddr("1234");
        messageEvent1.setDestinationAddr("432");
        messageEvent1.setShortMessage("anotherShortMessage");
        messageEvent1.setSourceAddrTon(2);
        messageEvent1.setSourceAddrNpi(2);
        messageEvent1.setDestAddrTon(2);
        messageEvent1.setDestAddrNpi(2);
        messageEvent1.setBroadcastId(1234);

        MessageEvent clonedEvent = messageEvent.clone(messageEvent1);

        assertNotEquals(messageEvent1, clonedEvent);
        assertEquals(messageEvent1.getId(), clonedEvent.getId());
    }
}