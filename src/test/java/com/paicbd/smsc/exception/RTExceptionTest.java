package com.paicbd.smsc.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class RTExceptionTest {
    @Test
    void testRTException_WithMessage() {
        String errorMessage = "This is a runtime exception";
        RTException exception = new RTException(errorMessage);

        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testRTException_WithMessageAndCause() {
        String errorMessage = "This is a runtime exception with a cause";
        Throwable cause = new Throwable("This is the cause");
        RTException exception = new RTException(errorMessage, cause);

        assertEquals(errorMessage, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void testRTException_WithoutParameters() {
        RTException exception = new RTException(null);

        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testRTException_WithNullCause() {
        String errorMessage = "This is a runtime exception with a null cause";
        RTException exception = new RTException(errorMessage, null);

        assertEquals(errorMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
}