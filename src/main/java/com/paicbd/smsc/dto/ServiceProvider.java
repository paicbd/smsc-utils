package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.paicbd.smsc.utils.Converter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceProvider {
    @JsonProperty("network_id")
    private int networkId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("system_id")
    private String systemId;

    @JsonProperty("password")
    private String password;

    @JsonProperty("system_type")
    private String systemType;

    @JsonProperty("interface_version")
    private String interfaceVersion;

    @JsonProperty("max_binds")
    private Integer maxBinds;

    @JsonProperty("current_binds_count")
    private Integer currentBindsCount;

    @JsonProperty("binds")
    private List<String> binds = new ArrayList<>();

    @JsonProperty("address_ton")
    private int addressTon;

    @JsonProperty("address_npi")
    private int addressNpi;

    @JsonProperty("address_range")
    private String addressRange;

    @JsonProperty("tps")
    private AtomicInteger tps = new AtomicInteger(0);

    @JsonProperty("is_prepaid")
    private Boolean isPrepaid;

    @JsonProperty("credit")
    private long credit;

    @JsonProperty("credit_used")
    private AtomicLong creditUsed = new AtomicLong(0);

    @JsonProperty("validity")
    private int validity;

    @JsonProperty("status")
    private String status;

    @JsonProperty("enabled")
    private int enabled;

    @JsonProperty("enquire_link_period")
    private int enquireLinkPeriod;

    @JsonProperty("pdu_timeout")
    private int pduTimeout;

    @JsonProperty("request_dlr")
    private Boolean requestDlr;

    @JsonProperty("has_available_credit")
    private Boolean hasAvailableCredit;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("contact_name")
    private String contactName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("callback_url")
    private String callbackUrl;

    @JsonProperty("authentication_types")
    private String authenticationTypes;

    @JsonProperty("header_security_name")
    private String headerSecurityName = "Authorization";

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("passwd")
    private String passwd;

    @JsonProperty("token")
    private String token;

    @JsonProperty("bind_type")
    private String bindType;

    @JsonProperty("callback_headers_http")
    private List<UtilsRecords.CallbackHeaderHttp> callbackHeadersHttp = new ArrayList<>();

    @Override
    public String toString() {
        return Converter.valueAsString(this);
    }
}
