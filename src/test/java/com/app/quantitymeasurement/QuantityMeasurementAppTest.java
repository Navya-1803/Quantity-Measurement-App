package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Equality() {
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES)));
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM)));
    }

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Conversion() {
        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), QuantityMeasurementApp.demonstrateConversion(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCHES));
    }

    @Test
    void testQuantityMeasurementApp_SimplifiedDemonstration_Addition() {
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), QuantityMeasurementApp.demonstrateAddition(
                new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES)));
        assertEquals(new Quantity<>(2000.0, WeightUnit.GRAM), QuantityMeasurementApp.demonstrateAddition(
                new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.GRAM));
    }

    @Test
    void testDemonstrationMethods_NullQuantity() {
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateEquality(null, new Quantity<>(1.0, LengthUnit.FEET)));
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateConversion(null, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> QuantityMeasurementApp.demonstrateAddition(null, new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testMain_RunsWithoutException() {
        assertDoesNotThrow(() -> QuantityMeasurementApp.main(new String[]{}));
    }
}
