package com.paicbd.smsc.dto;

import com.google.code.beanmatchers.BeanMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        gateway.setRequestDLR(true);
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
        String gwJson = "{\"network_id\":1,\"name\":\"Gateway\",\"system_id\":\"systemId\",\"password\":\"password\",\"ip\":\"localhost\",\"port\":2775,\"bind_type\":\"TRX\",\"system_type\":\"systemType\",\"interface_version\":\"3.4\",\"sessions_number\":1,\"address_ton\":1,\"address_npi\":1,\"address_range\":\"addressRange\",\"tps\":1,\"status\":\"STOPPED\",\"enabled\":1,\"enquire_link_period\":30000,\"enquire_link_timeout\":0,\"request_dlr\":true,\"no_retry_error_code\":\"1,2,3,4\",\"retry_alternate_destination_error_code\":\"5,6,7,8\",\"bind_timeout\":30000,\"bind_retry_period\":30000,\"pdu_timeout\":30000,\"pdu_degree\":1,\"thread_pool_size\":1,\"mno_id\":1,\"tlv_message_receipt_id\":true,\"message_id_decimal_format\":true,\"active_sessions_numbers\":1,\"protocol\":\"SMPP\",\"auto_retry_error_code\":\"1,2,3,4\",\"encoding_iso88591\":1,\"encoding_gsm7\":1,\"encoding_ucs2\":1,\"split_message\":true,\"split_smpp_type\":\"TLV\"}";
        assertEquals(gwJson, gateway.toString());
    }
}