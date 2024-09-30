package com.paicbd.smsc.utils;

import lombok.Getter;

@Getter
@Generated
public enum BroadcastMessageStatus {

    PENDING(1),
    ENQUEUE(2),
    SENT(3),
    FAILED(4);

    private final int value;

    BroadcastMessageStatus(int value) {
        this.value = value;
    }
}
