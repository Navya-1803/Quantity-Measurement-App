package com.app.quantitymeasurement.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    void testOf_FillsTimestamp() {
        ErrorResponse response = ErrorResponse.of(400, "Quantity Measurement Error", "bad unit", "/api/v1/quantities/add");
        assertNotNull(response.timestamp());
        assertEquals(400, response.status());
        assertEquals("bad unit", response.message());
        assertEquals("/api/v1/quantities/add", response.path());
    }
}
