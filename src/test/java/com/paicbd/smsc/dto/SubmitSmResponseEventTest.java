package com.paicbd.smsc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class SubmitSmResponseEventTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        assertThat(SubmitSmResponseEvent.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testToString() throws JsonProcessingException {
        SubmitSmResponseEvent submitSmResponseEvent = new SubmitSmResponseEvent(
                "hash-id", "1234", "", "systemId", "serverId", "SMPP", 1, "SP", null, null, null, "parent_id"
        );
        Assertions.assertEquals(submitSmResponseEvent.toString(), objectMapper.writeValueAsString(submitSmResponseEvent));

        SubmitSmResponseEvent submitSmResponseEvent2 = new SubmitSmResponseEvent();
        Assertions.assertEquals(submitSmResponseEvent2.toString(), objectMapper.writeValueAsString(submitSmResponseEvent2));
    }
}