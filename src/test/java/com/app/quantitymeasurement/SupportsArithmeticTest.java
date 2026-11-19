package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupportsArithmeticTest {

    @Test
    void testLambdaImplementation_True() {
        SupportsArithmetic supported = () -> true;
        assertTrue(supported.isSupported());
    }

    @Test
    void testLambdaImplementation_False() {
        SupportsArithmetic notSupported = () -> false;
        assertFalse(notSupported.isSupported());
    }

    @Test
    void testDefaultInIMeasurable_IsSupported() {
        assertTrue(IMeasurable.supportsArithmetic.isSupported());
    }

    @Test
    void testIsFunctionalInterface() {
        assertTrue(SupportsArithmetic.class.isAnnotationPresent(FunctionalInterface.class));
    }
}
