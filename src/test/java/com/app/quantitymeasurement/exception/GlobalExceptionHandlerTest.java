package com.app.quantitymeasurement.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/quantities/add");

    @Test
    void testExceptionHandling_QuantityException_Returns400() {
        ResponseEntity<ErrorResponse> response = handler.handleQuantityException(new QuantityMeasurementException("add Error: x"), request);
        assertEquals(400, response.getStatusCode().value());
        assertEquals("Quantity Measurement Error", response.getBody().error());
        assertEquals("/api/v1/quantities/add", response.getBody().path());
    }

    @Test
    void testExceptionHandling_IllegalArgument_Returns400() {
        assertEquals(400, handler.handleIllegalArgument(new IllegalArgumentException("Invalid operation"), request).getStatusCode().value());
    }

    @Test
    void testExceptionHandling_GlobalHandler_Returns500() {
        ResponseEntity<ErrorResponse> response = handler.handleGlobalException(new ArithmeticException("Cannot divide by zero quantity"), request);
        assertEquals(500, response.getStatusCode().value());
        assertEquals("Internal Server Error", response.getBody().error());
        assertEquals("Cannot divide by zero quantity", response.getBody().message());
    }
}
