package com.paicbd.smsc.utils;

import lombok.Getter;


@Getter
@Generated
public enum RequestDelivery {
    NON_REQUEST_DLR(0),
    REQUEST_DLR(1);

    private final int value;

    RequestDelivery(int value) {
        this.value = value;
    }

}
