package com.paicbd.smsc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ErrorCodeMappingTest {
    @Test
    void testGettersAndSetters() {
        assertThat(ErrorCodeMapping.class, BeanMatchers.hasValidGettersAndSetters());
    }
}