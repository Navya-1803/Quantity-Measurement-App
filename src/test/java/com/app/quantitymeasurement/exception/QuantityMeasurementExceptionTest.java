package com.app.quantitymeasurement.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementExceptionTest {

    @Test
    void testException_WithMessage() {
        QuantityMeasurementException exception = new QuantityMeasurementException("invalid unit");
        assertEquals("invalid unit", exception.getMessage());
    }

    @Test
    void testException_WithCause() {
        IllegalArgumentException cause = new IllegalArgumentException("root cause");
        QuantityMeasurementException exception = new QuantityMeasurementException("wrapped", cause);
        assertSame(cause, exception.getCause());
    }

    @Test
    void testException_IsUnchecked() {
        assertInstanceOf(RuntimeException.class, new QuantityMeasurementException("x"));
    }
}
