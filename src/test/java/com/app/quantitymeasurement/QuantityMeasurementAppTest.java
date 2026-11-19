package com.app.quantitymeasurement;

import com.app.quantitymeasurement.QuantityLength.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testDemonstrateLengthEquality_EquivalentValues() {
        assertTrue(QuantityMeasurementApp.demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testDemonstrateLengthEquality_DifferentValues() {
        assertFalse(QuantityMeasurementApp.demonstrateLengthEquality(
                new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(1.0, LengthUnit.INCHES)));
    }

    @Test
    void testDemonstrateLengthEquality_NullFirstOperand() {
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateLengthEquality(
                null, new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    void testMain_RunsWithoutException() {
        assertDoesNotThrow(() -> QuantityMeasurementApp.main(new String[]{}));
    }
}
