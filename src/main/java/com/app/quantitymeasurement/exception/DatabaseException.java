package com.app.quantitymeasurement.exception;

/**
 * DatabaseException wraps any failure that happens while talking to the
 * database, so the upper layers get one clear exception type.
 *
 * @author Navya Raj
 */
public class DatabaseException extends QuantityMeasurementException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
