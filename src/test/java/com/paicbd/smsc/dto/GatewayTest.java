package com.paicbd.smsc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GatewayTest {
    Gateway gateway;

    @BeforeEach
    void setUp() {
        gateway = new Gateway();
        gateway.setNetworkId(1);
        gateway.setName("Gateway");
        gateway.setSystemId("systemId");
        gateway.setPassword("password");
        gateway.setIp("localhost");
        gateway.setPort(2775);
        gateway.setBindType("TRX");
        gateway.setSystemType("systemType");
        gateway.setInterfaceVersion("3.4");
        gateway.setSessionsNumber(1);
        gateway.setAddressTON(1);
        gateway.setAddressNPI(1);
        gateway.setAddressRange("addressRange");
        gateway.setTps(1);
        gateway.setStatus("STOPPED");
        gateway.setEnabled(1);
        gateway.setEnquireLinkPeriod(30000);
        gateway.setEnquireLinkTimeout(0);
        gateway.setRequestDLR(1);
        gateway.setNoRetryErrorCode("1,2,3,4");
        gateway.setRetryAlternateDestinationErrorCode("5,6,7,8");
        gateway.setBindTimeout(30000);
        gateway.setBindRetryPeriod(30000);
        gateway.setPduTimeout(30000);
        gateway.setPduProcessorDegree(1);
        gateway.setThreadPoolSize(1);
        gateway.setMno(1);
        gateway.setTlvMessageReceiptId(true);
        gateway.setMessageIdDecimalFormat(true);
        gateway.setSuccessSession(1);
        gateway.setProtocol("SMPP");
        gateway.setAutoRetryErrorCode("1,2,3,4");
        gateway.setEncodingIso88591(1);
        gateway.setEncodingGsm7(1);
        gateway.setEncodingUcs2(1);
        gateway.setSplitMessage(true);
        gateway.setSplitSmppType("TLV");
    }

    @Test
    void testGettersAndSetters() {
        assertThat(Gateway.class, BeanMatchers.hasValidGettersAndSetters());
    }

    @Test
    void testToString() {
        assertDoesNotThrow(() -> gateway.toString());
    }
}