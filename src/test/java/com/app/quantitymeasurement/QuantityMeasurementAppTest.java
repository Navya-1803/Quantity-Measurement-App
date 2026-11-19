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

    @Test
    void testDemonstrateLengthConversion_Values() {
        assertEquals(12.0, QuantityMeasurementApp.demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES), 1e-6);
    }

    @Test
    void testDemonstrateLengthConversion_Quantity() {
        QuantityLength result = QuantityMeasurementApp.demonstrateLengthConversion(new QuantityLength(3.0, LengthUnit.YARDS), LengthUnit.FEET);
        assertEquals(new QuantityLength(9.0, LengthUnit.FEET), result);
    }

    @Test
    void testDemonstrateLengthConversion_NullQuantity() {
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateLengthConversion(null, LengthUnit.FEET));
    }

    @Test
    void testDemonstrateLengthAddition() {
        assertEquals(new QuantityLength(2.0, LengthUnit.FEET), QuantityMeasurementApp.demonstrateLengthAddition(
                new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testDemonstrateLengthAddition_NullFirstOperand() {
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateLengthAddition(
                null, new QuantityLength(1.0, LengthUnit.FEET)));
    }

    @Test
    void testDemonstrateLengthAddition_WithTargetUnit() {
        QuantityLength result = QuantityMeasurementApp.demonstrateLengthAddition(
                new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertEquals(new QuantityLength(24.0, LengthUnit.INCHES), result);
    }
}
