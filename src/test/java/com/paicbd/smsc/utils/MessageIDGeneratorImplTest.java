package com.paicbd.smsc.utils;

import com.paicbd.smsc.exception.RTException;
import org.jsmpp.util.MessageId;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageIDGeneratorImplTest {
    @Test
    void newMessageIdTest() {
        MessageIDGeneratorImpl messageIDGenerator = new MessageIDGeneratorImpl();
        String messageID = messageIDGenerator.newMessageId().toString();
        assertNotNull(messageID);
        assertNotNull(messageID.split("-")[0]);
        assertNotNull(messageID.split("-")[1]);
        MessageIDGeneratorImpl impl = new MessageIDGeneratorImpl() {
            @Override
            public MessageId newMessageId() {
                throw new RTException("Error while generating message ID");
            }
        };
        assertThrows(RuntimeException.class, impl::newMessageId);
    }
}