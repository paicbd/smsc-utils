package com.paicbd.smsc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class Ss7SettingsTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        assertThat(Ss7Settings.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testToString() throws JsonProcessingException {
        Ss7Settings ss7Settings = new Ss7Settings();
        Assertions.assertEquals(ss7Settings.toString(), objectMapper.writeValueAsString(ss7Settings));
    }
}