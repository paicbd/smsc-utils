package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.paicbd.smsc.utils.Converter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubmitSmResponseEvent {
    @JsonProperty("hash_id")
    private String hashId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("system_id")
    private String systemId;

    @JsonProperty("submit_sm_id")
    private String submitSmId;

    @JsonProperty("submit_sm_server_id")
    private String submitSmServerId;

    @JsonProperty("origin_protocol")
    private String originProtocol;

    @JsonProperty("origin_network_id")
    private int originNetworkId;

    @JsonProperty("origin_network_type")
    private String originNetworkType;

    @JsonProperty("msg_reference_number")
    private String msgReferenceNumber;

    @JsonProperty("total_segment")
    private Integer totalSegment;

    @JsonProperty("segment_sequence")
    private Integer segmentSequence;

    @JsonProperty("parent_id")
    private String parentId;

    @Override
    public String toString() {
        return Converter.valueAsString(this);
    }
}
