package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightUnitTest {

    private static final double DELTA = 1e-6;

    @Test
    void testWeightUnitEnum_KilogramConstant() {
        assertEquals(1.0, WeightUnit.KILOGRAM.getConversionFactor(), DELTA);
    }

    @Test
    void testWeightUnitEnum_GramConstant() {
        assertEquals(0.001, WeightUnit.GRAM.getConversionFactor(), DELTA);
    }

    @Test
    void testWeightUnitEnum_PoundConstant() {
        assertEquals(0.453592, WeightUnit.POUND.getConversionFactor(), DELTA);
    }

    @Test
    void testConvertToBaseUnit_GramToKilogram() {
        assertEquals(1.0, WeightUnit.GRAM.convertToBaseUnit(1000.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_PoundToKilogram() {
        assertEquals(0.453592, WeightUnit.POUND.convertToBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_KilogramToGram() {
        assertEquals(1000.0, WeightUnit.GRAM.convertFromBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_KilogramToPound() {
        assertEquals(2.20462, WeightUnit.POUND.convertFromBaseUnit(1.0), 1e-5);
    }

    @Test
    void testRoundTrip_AllUnits() {
        for (WeightUnit unit : WeightUnit.values()) {
            assertEquals(3.25, unit.convertFromBaseUnit(unit.convertToBaseUnit(3.25)), DELTA);
        }
    }
}
