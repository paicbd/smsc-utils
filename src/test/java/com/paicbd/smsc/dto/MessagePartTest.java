package com.paicbd.smsc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class MessagePartTest {
    @Test
    void testGettersAndSetters() {
        assertThat(MessagePart.class, BeanMatchers.hasValidGettersAndSetters());
    }
}