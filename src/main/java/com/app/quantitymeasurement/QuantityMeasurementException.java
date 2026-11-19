package com.app.quantitymeasurement;

/**
 * QuantityMeasurementException is thrown when a quantity measurement
 * operation fails, for example invalid units or incompatible categories.
 * It is unchecked so callers are not forced to catch it.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementException extends RuntimeException {

    public QuantityMeasurementException(String message) {
        super(message);
    }

    public QuantityMeasurementException(String message, Throwable cause) {
        super(message, cause);
    }
}
