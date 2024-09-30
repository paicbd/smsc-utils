package com.paicbd.smsc.cdr;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import com.paicbd.smsc.dto.UtilsRecords;
import com.paicbd.smsc.utils.UtilsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import redis.clients.jedis.JedisCluster;

@ExtendWith(MockitoExtension.class)
class CdrProcessorTest {
    private static final String CDR_DETAIL_HASH_NAME = "cdr_details";

    @Mock
    JedisCluster jedisCluster;

    CdrProcessor cdrProcessor;

    @BeforeEach
    void setUp() {
        cdrProcessor = new CdrProcessor(jedisCluster);
    }

    @Test
    void putCdrDetailOnRedis() {
        UtilsRecords.CdrDetail cdrDetail = new UtilsRecords.CdrDetail(
                1L,
                "1717535359756",
                "12345",
                1,
                2,
                "SMPP",
                "HTTP",
                "SP",
                "GW",
                1,
                "SMPP_CLIENT",
                "DELIVERD",
                "NOTHING",
                1,
                2,
                "1234",
                1,
                2,
                "4567",
                "imsi",
                null,
                "MESSAGE",
                0,
                null,
                true,
                "ENQUEUE",
                1L,
                2L,
                1,
                2,
                null,
                1,
                2,
                null,
                "011",
                null,
                "{}",
                "3",
                "120",
                1,
                3,
                "TEST MESSAGE",
                "1",
                1,
                2,
                null,
                123
        );
        when(jedisCluster.hset(CDR_DETAIL_HASH_NAME, cdrDetail.messageId(), cdrDetail.toString())).thenReturn(1L);
        assertDoesNotThrow(() -> cdrProcessor.putCdrDetailOnRedis(cdrDetail));
    }

    @Test
    void createCdrFailed() {
        String messageId = "1717535359756";
        when(jedisCluster.hget(CDR_DETAIL_HASH_NAME, messageId)).thenReturn(null);
        assertDoesNotThrow(() -> cdrProcessor.createCdr(messageId));
    }

    @Test
    void createCdrSuccess() {
        String cdrDetailJson = "{\"timestamp\":1719327181964,\"id_event\":\"1719327141615-2966989339944\",\"message_id\":\"1719327141615-2966989339944\",\"origin_network_id\":1,\"dest_network_id\":2,\"origin_protocol\":\"HTTP\",\"dest_protocol\":\"SS7\",\"origin_network_type\":\"SP\",\"dest_network_type\":\"GW\",\"routing_id\":1,\"module\":\"SS7_CLIENT\",\"status\":null,\"comment\":\"\",\"source_addr_ton\":4,\"source_addr_npi\":1,\"source_addr\":\"50510201020\",\"dest_addr_ton\":4,\"dest_addr_npi\":1,\"destination_addr\":\"50582368999\",\"imsi\":null,\"network_node_number\":null,\"message_type\":\"MESSAGE\",\"retry_number\":null,\"error_code\":null,\"is_dlr\":false,\"cdr_status\":\"SENT\",\"remote_dialog_id\":null,\"local_dialog_dd\":14,\"sccp_called_party_address_point_code\":0,\"sccp_called_party_address_sub_system_number\":6,\"sccp_called_party_address\":\"50582368999\",\"sccp_calling_party_address_point_code\":0,\"sccp_calling_party_address_sub_system_number\":8,\"sccp_calling_party_address\":\"22220\",\"global_title\":\"22220\",\"originator_sccp_address\":null,\"udhi\":null,\"esm_class\":\"67\",\"validity_period\":\"60\",\"registered_delivery\":1,\"data_coding\":0,\"message\":\"Java \",\"msg_reference_number\":null,\"total_segment\":null,\"segment_sequence\":null,\"parent_id\":\"1719327141615-2966989339944\", \"broadcast_id\":null }";
        String messageId = "1719327141615-2966989339944";
        when(jedisCluster.hget(CDR_DETAIL_HASH_NAME, messageId)).thenReturn(cdrDetailJson);
        assertDoesNotThrow(() -> cdrProcessor.createCdr(messageId));
    }

    private void testCdr(UtilsEnum.CdrStatus cdrStatus) {
        String cdrDetailJson = "{\"timestamp\":1719327181964,\"id_event\":\"1719327141615-2966989339944\",\"message_id\":\"1719327141615-2966989339944\",\"origin_network_id\":1,\"dest_network_id\":2,\"origin_protocol\":\"HTTP\",\"dest_protocol\":\"SS7\",\"origin_network_type\":\"SP\",\"dest_network_type\":\"GW\",\"routing_id\":1,\"module\":\"SS7_CLIENT\",\"status\":null,\"comment\":\"\",\"source_addr_ton\":4,\"source_addr_npi\":1,\"source_addr\":\"50510201020\",\"dest_addr_ton\":4,\"dest_addr_npi\":1,\"destination_addr\":\"50582368999\",\"imsi\":null,\"network_node_number\":null,\"message_type\":\"MESSAGE\",\"retry_number\":null,\"error_code\":null,\"is_dlr\":false,\"cdr_status\":\"SENT\",\"remote_dialog_id\":null,\"local_dialog_dd\":14,\"sccp_called_party_address_point_code\":0,\"sccp_called_party_address_sub_system_number\":6,\"sccp_called_party_address\":\"50582368999\",\"sccp_calling_party_address_point_code\":0,\"sccp_calling_party_address_sub_system_number\":8,\"sccp_calling_party_address\":\"22220\",\"global_title\":\"22220\",\"originator_sccp_address\":null,\"udhi\":null,\"esm_class\":\"67\",\"validity_period\":\"60\",\"registered_delivery\":1,\"data_coding\":0,\"message\":\"Java \",\"msg_reference_number\":null,\"total_segment\":null,\"segment_sequence\":null,\"parent_id\":\"1719327141615-2966989339944\", \"broadcast_id\":14 }";
        String messageId = "1719327141615-2966989339944";
        cdrDetailJson = cdrDetailJson.replace("\"SENT\"", "\"" + cdrStatus.name() + "\"");
        when(jedisCluster.hget(CDR_DETAIL_HASH_NAME, messageId)).thenReturn(cdrDetailJson);
        assertDoesNotThrow(() -> cdrProcessor.createCdr(messageId));
    }

    @Test
    void createCdrSuccessWithBroadcastIdSuccess() {
        testCdr(UtilsEnum.CdrStatus.SENT);
    }

    @Test
    void createCdrSuccessWithBroadcastIdFailed() {
        testCdr(UtilsEnum.CdrStatus.FAILED);
    }
}
