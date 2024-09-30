package com.paicbd.smsc.utils;

import com.paicbd.smsc.exception.RTException;
import lombok.extern.slf4j.Slf4j;
import org.jsmpp.PDUStringException;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;

@Slf4j
public class MessageIDGeneratorImpl implements MessageIDGenerator {
    @Override
    public MessageId newMessageId() {
        String messageId = System.currentTimeMillis() + "-" + System.nanoTime();
        try {
            return new MessageId(messageId);
        } catch (PDUStringException e) {
            log.error("Error on generate messageId {}" , messageId);
            throw new RTException("Failed to generate message id", e);
        }
    }
}
