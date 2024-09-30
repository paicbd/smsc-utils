package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paicbd.smsc.utils.Converter;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UtilsRecords {
    public record WebSocketConnectionParams(
            boolean isWsEnabled,
            String host,
            int port,
            String path,
            List<String> topicsToSubscribe,
            String headerKey,
            String headerValue,
            int retryInterval,
            String module
    ) {
        @Override
        public String toString() {
            return Converter.valueAsString(this);
        }
    }

    public record OptionalParameter(
            short tag, String value
    ) {
    }

    public record SubmitSmResponseEvent(
            @JsonProperty("hash_id") String hashId,
            @JsonProperty("id") String id,
            @JsonProperty("system_id") String systemId,
            @JsonProperty("submit_sm_id") String submitSmId,
            @JsonProperty("submit_sm_server_id") String submitSmServerId,
            @JsonProperty("origin_protocol") String originProtocol,
            @JsonProperty("origin_network_id") int originNetworkId,
            @JsonProperty("origin_network_type") String originNetworkType,
            @JsonProperty("msg_reference_number") String msgReferenceNumber,
            @JsonProperty("total_segment") Integer totalSegment,
            @JsonProperty("segment_sequence") Integer segmentSequence,
            @JsonProperty("parent_id") String parentId
    ) {
        @Override
        public String toString() {
            return Converter.valueAsString(this);
        }
    }

    public record JedisConfigParams(
            @Nonnull List<String> redisNodes,
            int maxTotal,
            int maxIdle,
            int minIdle,
            boolean blockWhenExhausted
    ) {
        @Override
        public String toString() {
            return Converter.valueAsString(this);
        }
    }

    public record CdrDetail(
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("id_event") String idEvent,
            @JsonProperty("message_id") String messageId,
            @JsonProperty("origin_network_id") Integer originNetworkId,
            @JsonProperty("dest_network_id") Integer destNetworkId,
            @JsonProperty("origin_protocol") String originProtocol,
            @JsonProperty("dest_protocol") String destProtocol,
            @JsonProperty("origin_network_type") String originNetworkType,
            @JsonProperty("dest_network_type") String destNetworkType,
            @JsonProperty("routing_id") int routingId,
            @JsonProperty("module") String module,
            @JsonProperty("status") String status,
            @JsonProperty("comment") String comment,
            @JsonProperty("source_addr_ton") Integer sourceAddrTon,
            @JsonProperty("source_addr_npi") Integer sourceAddrNpi,
            @JsonProperty("source_addr") String sourceAddr,
            @JsonProperty("dest_addr_ton") Integer destAddrTon,
            @JsonProperty("dest_addr_npi") Integer destAddrNpi,
            @JsonProperty("destination_addr") String destinationAddr,
            @JsonProperty("imsi") String imsi,
            @JsonProperty("network_node_number") String networkNodeNumber,
            @JsonProperty("message_type") String messageType,
            @JsonProperty("retry_number") Integer retryNumber,
            @JsonProperty("error_code") String errorCode,
            @JsonProperty("is_dlr") boolean isDlr,
            @JsonProperty("cdr_status") String cdrStatus,
            @JsonProperty("remote_dialog_id") Long remoteDialogId,
            @JsonProperty("local_dialog_dd") Long localDialogId,
            @JsonProperty("sccp_called_party_address_point_code") Integer sccpCalledPartyAddressPointCode,
            @JsonProperty("sccp_called_party_address_sub_system_number") Integer sccpCalledPartyAddressSubSystemNumber,
            @JsonProperty("sccp_called_party_address") String sccpCalledPartyAddress,
            @JsonProperty("sccp_calling_party_address_point_code") Integer sccpCallingPartyAddressPointCode,
            @JsonProperty("sccp_calling_party_address_sub_system_number") Integer sccpCallingPartyAddressSubSystemNumber,
            @JsonProperty("sccp_calling_party_address") String sccpCallingPartyAddress,
            @JsonProperty("global_title") String globalTitle,
            @JsonProperty("originator_sccp_address") String originatorSccpAddress,
            @JsonProperty("udhi") String udhi,
            @JsonProperty("esm_class") String esmClass,
            @JsonProperty("validity_period") String validityPeriod,
            @JsonProperty("registered_delivery") Integer registeredDelivery,
            @JsonProperty("data_coding") int dataCoding,
            @JsonProperty("message") String message,
            @JsonProperty("msg_reference_number") String msgReferenceNumber,
            @JsonProperty("total_segment") Integer totalSegment,
            @JsonProperty("segment_sequence") Integer segmentSequence,
            @JsonProperty("parent_id") String parentId,
            @JsonProperty("broadcast_id") Integer broadcastId
    ) {
        @Override
        public String toString() {
            return Converter.valueAsString(this);
        }
    }

    public record Cdr(
            @JsonProperty("record_date") String recordDate,
            @JsonProperty("submit_date") String submitDate,
            @JsonProperty("delivery_date") String deliveryDate,
            @JsonProperty("message_type") String messageType,
            @JsonProperty("message_id") String messageId,
            @JsonProperty("origination_protocol") String originationProtocol,
            @JsonProperty("origination_network_id") String originationNetworkId,
            @JsonProperty("origination_type") String originationType,
            @JsonProperty("destination_protocol") String destinationProtocol,
            @JsonProperty("destination_network_id") String destinationNetworkId,
            @JsonProperty("destination_type") String destinationType,
            @JsonProperty("routing_id") String routingId,
            @JsonProperty("status") String status,
            @JsonProperty("status_code") String statusCode,
            @JsonProperty("comment") String comment,
            @JsonProperty("dialog_duration") String dialogDuration,
            @JsonProperty("processing_time") String processingTime,
            @JsonProperty("data_coding") String dataCoding,
            @JsonProperty("validity_period") String validityPeriod,
            @JsonProperty("addr_src_digits") String addrSrcDigits,
            @JsonProperty("addr_src_ton") String addrSrcTon,
            @JsonProperty("addr_src_npi") String addrSrcNpi,
            @JsonProperty("addr_dst_digits") String addrDstDigits,
            @JsonProperty("addr_dst_ton") String addrDstTon,
            @JsonProperty("addr_dst_npi") String addrDstNpi,
            @JsonProperty("remote_dialog_id") String remoteDialogId,
            @JsonProperty("local_dialog_id") String localDialogId,
            @JsonProperty("local_spc") String localSpc,
            @JsonProperty("local_ssn") String localSsn,
            @JsonProperty("local_global_title_digits") String localGlobalTitleDigits,
            @JsonProperty("remote_spc") String remoteSpc,
            @JsonProperty("remote_ssn") String remoteSsn,
            @JsonProperty("remote_global_title_digits") String remoteGlobalTitleDigits,
            @JsonProperty("imsi") String imsi,
            @JsonProperty("nnn_digits") String nnnDigits,
            @JsonProperty("originator_sccp_address") String originatorSccpAddress,
            @JsonProperty("mt_service_center_address") String mtServiceCenterAddress,
            @JsonProperty("first_20_character_of_sms") String first20CharacterOfSms,
            @JsonProperty("esm_class") String esmClass,
            @JsonProperty("udhi") String udhi,
            @JsonProperty("registered_delivery") String registeredDelivery,
            @JsonProperty("msg_reference_number") String msgReferenceNumber,
            @JsonProperty("total_segment") String totalSegment,
            @JsonProperty("segment_sequence") String segmentSequence,
            @JsonProperty("retry_number") String retryNumber,
            @JsonProperty("parent_id") String parentId
    ) {
    }

    public record CallbackHeaderHttp(
            @JsonProperty("header_name")
            String headerName,

            @JsonProperty("header_value")
            String headerValue
    ) {
    }


    public record BroadcastStatistic(
            @JsonProperty("broadcast_id") Integer broadcastId,
            @JsonProperty("message_id") String messageId,
            @JsonProperty("status") Integer status,
            @JsonProperty("date") String date
    ) {
        @Override
        public String toString() {
            return Converter.valueAsString(this);
        }
    }
}
