package com.paicbd.smsc.exception;

public class RTException extends RuntimeException {
    public RTException(String message) {
        super(message);
    }

    public RTException(String message, Throwable cause) {
        super(message, cause);
    }
}
