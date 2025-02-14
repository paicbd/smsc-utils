package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paicbd.smsc.utils.Converter;
import com.paicbd.smsc.utils.UtilsEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEvent {
    @JsonProperty("id")
    private String id;

    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("system_id")
    private String systemId;

    @JsonProperty("deliver_sm_id")
    private String deliverSmId;

    @JsonProperty("deliver_sm_server_id")
    private String deliverSmServerId;

    @JsonProperty("command_status")
    private int commandStatus;

    @JsonProperty("sequence_number")
    private int sequenceNumber;

    // NatureAddressIndicator
    @JsonProperty("source_addr_ton")
    private Integer sourceAddrTon;

    // NumberingPlan
    @JsonProperty("source_addr_npi")
    private Integer sourceAddrNpi;

    @JsonProperty("source_addr")
    private String sourceAddr;

    // NatureAddressIndicator
    @JsonProperty("dest_addr_ton")
    private Integer destAddrTon;

    // NumberingPlan
    @JsonProperty("dest_addr_npi")
    private Integer destAddrNpi;

    @JsonProperty("destination_addr")
    private String destinationAddr;

    @JsonProperty("command_id")
    private int commandId;

    @JsonProperty("command_length")
    private int commandLength;

    @JsonProperty("service_type")
    private String serviceType;

    @JsonProperty("schedule_delivery_time")
    private String scheduleDeliveryTime;

    @JsonProperty("protocol_id")
    private byte protocolId;

    @JsonProperty("priority_flag")
    private byte priorityFlag;

    @JsonProperty("replace_if_present")
    private byte replaceIfPresent;

    @JsonProperty("esm_class")
    private Integer esmClass;

    @JsonProperty("validity_period")
    private long validityPeriod;

    @JsonProperty("string_validity_period")
    private String stringValidityPeriod;

    @JsonProperty("registered_delivery")
    private Integer registeredDelivery;

    @JsonProperty("data_coding")
    private Integer dataCoding;

    @JsonProperty("sm_default_msg_id")
    private int smDefaultMsgId;

    @JsonProperty("short_message")
    private String shortMessage;

    @JsonProperty("delivery_receipt")
    private String delReceipt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error_code")
    private String errorCode;

    @JsonProperty("check_submit_sm_response")
    private Boolean checkSubmitSmResponse;

    @JsonProperty("optional_parameters")
    private List<UtilsRecords.OptionalParameter> optionalParameters;

    @JsonProperty("origin_network_type")
    private String originNetworkType;

    @JsonProperty("origin_protocol")
    private String originProtocol;

    @JsonProperty("origin_network_id")
    private int originNetworkId;

    @JsonProperty("dest_network_type")
    private String destNetworkType;

    @JsonProperty("dest_protocol")
    private String destProtocol;

    @JsonProperty("dest_network_id")
    private int destNetworkId;

    @JsonProperty("routing_id")
    private int routingId;

    private String msisdn;

    @JsonProperty("address_nature_msisdn")
    private Integer addressNatureMsisdn;

    @JsonProperty("numbering_plan_msisdn")
    private Integer numberingPlanMsisdn;

    @JsonProperty("remote_dialog_id")
    private Long remoteDialogId;

    @JsonProperty("local_dialog_id")
    private Long localDialogId;

    @JsonProperty("sccp_called_party_address_pc")
    private Integer sccpCalledPartyAddressPointCode;

    @JsonProperty("sccp_called_party_address_ssn")
    private Integer sccpCalledPartyAddressSubSystemNumber;

    @JsonProperty("sccp_called_party_address")
    private String sccpCalledPartyAddress;

    @JsonProperty("sccp_calling_party_address_pc")
    private Integer sccpCallingPartyAddressPointCode;

    @JsonProperty("sccp_calling_party_address_ssn")
    private Integer sccpCallingPartyAddressSubSystemNumber;

    @JsonProperty("sccp_calling_party_address")
    private String sccpCallingPartyAddress;

    //GT of the Gateway
    @JsonProperty("global_title")
    private String globalTitle;

    @JsonProperty("global_title_indicator")
    private String globalTitleIndicator;

    @JsonProperty("translation_type")
    private Integer translationType;

    @JsonProperty("smsc_ssn")
    private Integer smscSsn;

    @JsonProperty("hlr_ssn")
    private Integer hlrSsn;

    @JsonProperty("msc_ssn")
    private Integer mscSsn;

    @JsonProperty("map_version")
    private Integer mapVersion;

    @JsonProperty("is_retry")
    private boolean isRetry;

    @JsonProperty("retry_dest_network_id")
    private String retryDestNetworkId;

    @JsonProperty("retry_number")
    private Integer retryNumber;

    @JsonProperty("is_last_retry")
    private boolean isLastRetry;

    @JsonProperty("is_network_notify_error")
    private boolean isNetworkNotifyError;

    @JsonProperty("due_delay")
    private int dueDelay;

    @JsonProperty("accumulated_time")
    private int accumulatedTime;

    @JsonProperty("drop_map_sri")
    private boolean dropMapSri;

    @JsonProperty("network_id_to_map_sri")
    private int networkIdToMapSri;

    @JsonProperty("network_id_to_permanent_failure")
    private int networkIdToPermanentFailure;

    @JsonProperty("drop_temp_failure")
    private boolean dropTempFailure;

    @JsonProperty("network_id_temp_failure")
    private int networkIdTempFailure;

    @JsonProperty("imsi")
    private String imsi;

    @JsonProperty("network_node_number")
    private String networkNodeNumber;

    @JsonProperty("network_node_number_nature_of_address")
    private Integer networkNodeNumberNatureOfAddress;

    @JsonProperty("network_node_number_numbering_plan")
    private Integer networkNodeNumberNumberingPlan;

    @JsonProperty("mo_message")
    private boolean moMessage;

    @JsonProperty("is_sri_response")
    private boolean sriResponse = false;

    @JsonProperty("check_sri_response")
    private boolean checkSriResponse = false;

    @JsonProperty("msg_reference_number")
    private String msgReferenceNumber;

    @JsonProperty("total_segment")
    private Integer totalSegment;

    @JsonProperty("segment_sequence")
    private Integer segmentSequence;

    @JsonProperty("originator_sccp_address")
    private String originatorSccpAddress;

    @JsonProperty("udhi")
    private String udhi;

    @JsonProperty("udh_json")
    private String udhJson;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("is_dlr")
    private boolean isDlr;

    @JsonProperty("message_parts")
    private List<MessagePart> messageParts;

    @JsonIgnore
    private boolean process = true;

    @JsonProperty("broadcast_id")
    private Integer broadcastId;

    @JsonProperty("custom_parameters")
    Map<String, Object> customParams;

    public MessageEvent clone(MessageEvent event) {
        this.id = event.id;
        this.messageId = event.messageId;
        this.systemId = event.systemId;
        this.deliverSmId = event.deliverSmId;
        this.deliverSmServerId = event.deliverSmServerId;
        this.commandStatus = event.commandStatus;
        this.sequenceNumber = event.sequenceNumber;
        this.sourceAddrTon = event.sourceAddrTon;
        this.sourceAddrNpi = event.sourceAddrNpi;
        this.sourceAddr = event.sourceAddr;
        this.destAddrTon = event.destAddrTon;
        this.destAddrNpi = event.destAddrNpi;
        this.destinationAddr = event.destinationAddr;
        this.esmClass = event.esmClass;
        this.validityPeriod = event.validityPeriod;
        this.stringValidityPeriod = event.stringValidityPeriod;
        this.registeredDelivery = event.registeredDelivery;
        this.dataCoding = event.dataCoding;
        this.smDefaultMsgId = event.smDefaultMsgId;
        this.shortMessage = event.shortMessage;
        this.delReceipt = event.delReceipt;
        this.status = event.status;
        this.errorCode = event.errorCode;
        this.checkSubmitSmResponse = event.checkSubmitSmResponse;
        this.optionalParameters = event.optionalParameters;
        this.originNetworkType = event.originNetworkType;
        this.originProtocol = event.originProtocol;
        this.originNetworkId = event.originNetworkId;
        this.destNetworkType = event.destNetworkType;
        this.destProtocol = event.destProtocol;
        this.destNetworkId = event.destNetworkId;
        this.routingId = event.routingId;
        this.msisdn = event.msisdn;
        this.addressNatureMsisdn = event.addressNatureMsisdn;
        this.numberingPlanMsisdn = event.numberingPlanMsisdn;
        this.remoteDialogId = event.remoteDialogId;
        this.localDialogId = event.localDialogId;
        this.sccpCalledPartyAddressPointCode = event.sccpCalledPartyAddressPointCode;
        this.sccpCalledPartyAddressSubSystemNumber = event.sccpCalledPartyAddressSubSystemNumber;
        this.sccpCalledPartyAddress = event.sccpCalledPartyAddress;
        this.sccpCallingPartyAddressPointCode = event.sccpCallingPartyAddressPointCode;
        this.sccpCallingPartyAddressSubSystemNumber = event.sccpCallingPartyAddressSubSystemNumber;
        this.sccpCallingPartyAddress = event.sccpCallingPartyAddress;
        this.globalTitle = event.globalTitle;
        this.globalTitleIndicator = event.globalTitleIndicator;
        this.translationType = event.translationType;
        this.smscSsn = event.smscSsn;
        this.hlrSsn = event.hlrSsn;
        this.mscSsn = event.mscSsn;
        this.mapVersion = event.mapVersion;
        this.isRetry = event.isRetry;
        this.retryDestNetworkId = event.retryDestNetworkId;
        this.retryNumber = event.retryNumber;
        this.isLastRetry = event.isLastRetry;
        this.isNetworkNotifyError = event.isNetworkNotifyError;
        this.dueDelay = event.dueDelay;
        this.accumulatedTime = event.accumulatedTime;
        this.dropMapSri = event.dropMapSri;
        this.networkIdToMapSri = event.networkIdToMapSri;
        this.networkIdToPermanentFailure = event.networkIdToPermanentFailure;
        this.dropTempFailure = event.dropTempFailure;
        this.networkIdTempFailure = event.networkIdTempFailure;
        this.imsi = event.imsi;
        this.networkNodeNumber = event.networkNodeNumber;
        this.networkNodeNumberNatureOfAddress = event.networkNodeNumberNatureOfAddress;
        this.networkNodeNumberNumberingPlan = event.networkNodeNumberNumberingPlan;
        this.moMessage = event.moMessage;
        this.sriResponse = event.sriResponse;
        this.checkSriResponse = event.checkSriResponse;
        this.msgReferenceNumber = event.msgReferenceNumber;
        this.totalSegment = event.totalSegment;
        this.segmentSequence = event.segmentSequence;
        this.originatorSccpAddress = event.originatorSccpAddress;
        this.udhi = event.udhi;
        this.udhJson = event.udhJson;
        this.isDlr = event.isDlr;
        this.process = event.process;
        this.parentId = event.parentId;
        this.broadcastId = event.broadcastId;
        this.customParams = event.customParams;
        return this;
    }

    public UtilsRecords.CdrDetail toCdrDetail(UtilsEnum.Module module, UtilsEnum.MessageType messageType, UtilsEnum.CdrStatus cdrStatus, String comment) {
        return new UtilsRecords.CdrDetail(
                System.currentTimeMillis(), this.id, this.messageId, this.originNetworkId, this.destNetworkId,
                this.originProtocol, this.destProtocol, this.originNetworkType, this.destNetworkType, this.routingId,
                module.name(), this.status, comment, this.sourceAddrTon, this.sourceAddrNpi, this.sourceAddr, this.destAddrTon,
                this.destAddrNpi, this.destinationAddr, this.imsi, this.networkNodeNumber, messageType.name(),
                this.retryNumber, this.errorCode, this.isDlr, cdrStatus.name(), this.remoteDialogId, this.localDialogId,
                this.sccpCalledPartyAddressPointCode, this.sccpCalledPartyAddressSubSystemNumber, this.sccpCalledPartyAddress,
                this.sccpCallingPartyAddressPointCode, this.sccpCallingPartyAddressSubSystemNumber, this.sccpCallingPartyAddress,
                this.globalTitle, this.originatorSccpAddress, this.udhi, (this.esmClass != null) ? this.esmClass.toString() : "",
                this.validityPeriod, this.registeredDelivery, (this.dataCoding != null) ? this.dataCoding : 0, this.obtainFirstChars(), this.msgReferenceNumber,
                this.totalSegment, this.segmentSequence, this.parentId, this.broadcastId
        );
    }

    public void transformToSs7MtDeliver() {
        String newSourceAddr = this.destinationAddr;
        String newDestinationAddr = this.sourceAddr;
        int newSourceAddrTon = this.destAddrTon;
        int newDestAddrTon = this.sourceAddrTon;
        int newSourceAddrNpi = this.destAddrNpi;
        int newDestAddrNpi = this.sourceAddrNpi;

        this.sourceAddr = newSourceAddr;
        this.destinationAddr = newDestinationAddr;
        this.sourceAddrTon = newSourceAddrTon;
        this.destAddrTon = newDestAddrTon;
        this.sourceAddrNpi = newSourceAddrNpi;
        this.destAddrNpi = newDestAddrNpi;
        this.msisdn = newDestinationAddr;
        this.setImsi(null); // If the IMSI is null, it means that is needed to send SRI Request, else, it is a normal MT SMS
        this.setDlr(true);
        this.setMsisdn(msisdn);
    }

    private String obtainFirstChars() {
        if (Objects.isNull(this.shortMessage)) return "";
        return this.shortMessage.length() > 20 ? this.shortMessage.substring(0, 20) : this.shortMessage;
    }

    public String obtainCustomParentId() {
        if (Objects.isNull(this.shortMessage)) return null;
        if (this.shortMessage.startsWith("id:")
                && this.segmentSequence != null
                && this.totalSegment > 1) {
            return this.shortMessage.substring(3, this.shortMessage.indexOf(" "));
        }
        return null;
    }

    public boolean notApplyForLongMessage() {
        return "SP".equalsIgnoreCase(this.destNetworkType) || "HTTP".equalsIgnoreCase(this.getDestProtocol()) || (Objects.nonNull(this.messageParts) && !this.messageParts.isEmpty());
    }

    public boolean applyForLongMessage() {
        return "GW".equalsIgnoreCase(this.destNetworkType) && !"HTTP".equalsIgnoreCase(this.getDestProtocol()) && (Objects.isNull(this.messageParts) || this.messageParts.isEmpty());
    }

    @Override
    public String toString() {
        return Converter.valueAsString(this);
    }
}
