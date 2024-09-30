package com.paicbd.smsc.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ServiceProviderTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGettersAndSetters() {
        assertThat(ServiceProvider.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testToString() throws JsonProcessingException {
        ServiceProvider serviceProvider = new ServiceProvider();
        Assertions.assertEquals(serviceProvider.toString(), objectMapper.writeValueAsString(serviceProvider));
    }
}