package com.app.quantitymeasurement.exception;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseExceptionTest {

    @Test
    void testDatabaseException_CustomException() {
        DatabaseException exception = new DatabaseException("connection failed");
        assertEquals("connection failed", exception.getMessage());
        assertInstanceOf(QuantityMeasurementException.class, exception);
    }

    @Test
    void testDatabaseException_WithCause() {
        SQLException cause = new SQLException("table missing");
        assertSame(cause, new DatabaseException("query failed", cause).getCause());
    }
}
