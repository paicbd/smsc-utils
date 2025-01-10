package com.paicbd.smsc.utils;

import com.paicbd.smsc.exception.RTException;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;


@Getter
@Generated
public enum RequestDelivery {
    NON_REQUEST_DLR(0),
    REQUEST_DLR(1),
    TRANSPARENT(2);

    private final int value;

    RequestDelivery(int value) {
        this.value = value;
    }

    public static RequestDelivery fromInt(Integer intValue) {
        Objects.requireNonNull(intValue);

        return Arrays.stream(RequestDelivery.values())
                .filter(requestDelivery -> requestDelivery.getValue() == intValue)
                .findFirst()
                .orElseThrow(() -> new RTException("Unexpected value: " + intValue));
    }
}
