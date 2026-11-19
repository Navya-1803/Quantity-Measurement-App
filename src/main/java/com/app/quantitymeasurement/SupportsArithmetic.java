package com.app.quantitymeasurement;

/**
 * Functional interface telling whether a unit category supports
 * arithmetic operations like add, subtract and divide.
 *
 * @author Navya Raj
 */
@FunctionalInterface
public interface SupportsArithmetic {

    boolean isSupported();
}
