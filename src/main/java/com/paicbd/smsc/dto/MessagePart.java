package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MessagePart {
    @JsonProperty("message_id")
    private String messageId;

    @JsonProperty("short_message")
    private String shortMessage;

    @JsonProperty("msg_reference_number")
    private String msgReferenceNumber;

    @JsonProperty("total_segment")
    private Integer totalSegment;

    @JsonProperty("segment_sequence")
    private Integer segmentSequence;

    @JsonProperty("udh_json")
    private String udhJson;

    @JsonProperty("optional_parameters")
    private List<UtilsRecords.OptionalParameter> optionalParameters;
}
