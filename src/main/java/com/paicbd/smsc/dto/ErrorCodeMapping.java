package com.paicbd.smsc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorCodeMapping {
    @JsonProperty("error_code")
    private int errorCode;

    @JsonProperty("delivery_error_code")
    private int deliveryErrorCode;

    @JsonProperty("delivery_status")
    private String deliveryStatus;
}
