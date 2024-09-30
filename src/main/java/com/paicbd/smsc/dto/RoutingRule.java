package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoutingRule {
    @JsonProperty("id")
    private int id;

    @JsonProperty("origin_network_id")
    private int originNetworkId;

    @JsonProperty("origin_network_type")
    private String originNetworkType;

    @JsonProperty("regex_source_addr")
    private String originRegexSourceAddr;

    @JsonProperty("regex_source_addr_ton")
    private String originRegexSourceAddrTon;

    @JsonProperty("regex_source_addr_npi")
    private String originRegexSourceAddrNpi;

    @JsonProperty("regex_destination_addr")
    private String originRegexDestinationAddr;

    @JsonProperty("regex_dest_addr_ton")
    private String originRegexDestAddrTon;

    @JsonProperty("regex_dest_addr_npi")
    private String originRegexDestAddrNpi;

    @JsonProperty("regex_imsi_digits_mask")
    private String regexImsiDigitsMask;

    @JsonProperty("regex_network_node_number")
    private String regexNetworkNodeNumber;

    @JsonProperty("regex_calling_party_address")
    private String regexCallingPartyAddress;

    @JsonProperty("destination")
    private List<Destination> destination;

    @JsonProperty("new_source_addr")
    private String newSourceAddr;

    @JsonProperty("new_source_addr_ton")
    private int newSourceAddrTon;

    @JsonProperty("new_source_addr_npi")
    private int newSourceAddrNpi;

    @JsonProperty("new_destination_addr")
    private String newDestinationAddr;

    @JsonProperty("new_dest_addr_ton")
    private int newDestAddrTon;

    @JsonProperty("new_dest_addr_npi")
    private int newDestAddrNpi;

    @JsonProperty("origin_protocol")
    private String originProtocol;

    @JsonProperty("add_source_addr_prefix")
    private String addSourceAddrPrefix;

    @JsonProperty("remove_source_addr_prefix")
    private String removeSourceAddrPrefix;

    @JsonProperty("add_dest_addr_prefix")
    private String addDestAddrPrefix;

    @JsonProperty("remove_dest_addr_prefix")
    private String removeDestAddrPrefix;

    @JsonProperty("new_gt_sccp_addr_mt")
    private String newGtSccpAddrMt;

    @JsonProperty("drop_map_sri")
    private boolean dropMapSri = false;

    @JsonProperty("network_id_to_map_sri")
    private int networkIdToMapSri;

    @JsonProperty("network_id_to_permanent_failure")
    private int networkIdToPermanentFailure;

    @JsonProperty("drop_temp_failure")
    private boolean dropTempFailure = false;

    @JsonProperty("network_id_temp_failure")
    private int networkIdTempFailure;

    @JsonProperty("has_filter_rules")
    private boolean hasFilterRules = false;

    @JsonProperty("has_action_rules")
    private boolean hasActionRules = false;

    @JsonProperty("is_sri_response")
    private boolean sriResponse = false;

    @JsonProperty("check_sri_response")
    private boolean checkSriResponse = false;

    @Getter
    @Setter
    public static class Destination {
        @JsonProperty("priority")
        private int priority;
        @JsonProperty("network_id")
        private int networkId;
        @JsonProperty("dest_protocol")
        private String protocol;
        @JsonProperty("network_type")
        private String networkType;
    }
}
