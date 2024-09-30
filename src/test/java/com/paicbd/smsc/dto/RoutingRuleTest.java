package com.paicbd.smsc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class RoutingRuleTest {
    @Test
    void testGettersAndSetters() {
        assertThat(RoutingRule.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testDestinationGettersAndSetters() {
        assertThat(RoutingRule.Destination.class, BeanMatchers.hasValidGettersAndSetters());
    }
}