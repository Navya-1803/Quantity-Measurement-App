package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LengthUnitTest {

    private static final double DELTA = 1e-6;

    @Test
    void testLengthUnitEnum_FeetConstant() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), DELTA);
    }

    @Test
    void testLengthUnitEnum_InchesConstant() {
        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), DELTA);
    }

    @Test
    void testLengthUnitEnum_YardsConstant() {
        assertEquals(3.0, LengthUnit.YARDS.getConversionFactor(), DELTA);
    }

    @Test
    void testLengthUnitEnum_CentimetersConstant() {
        assertEquals(0.393701 / 12.0, LengthUnit.CENTIMETERS.getConversionFactor(), DELTA);
    }

    @Test
    void testConvertToBaseUnit_FeetToFeet() {
        assertEquals(12.0, LengthUnit.FEET.convertToBaseUnit(12.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_InchesToFeet() {
        assertEquals(1.0, LengthUnit.INCHES.convertToBaseUnit(12.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_YardsToFeet() {
        assertEquals(3.0, LengthUnit.YARDS.convertToBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_CentimetersToFeet() {
        assertEquals(1.0, LengthUnit.CENTIMETERS.convertToBaseUnit(30.48), 1e-4);
    }

    @Test
    void testConvertFromBaseUnit_FeetToFeet() {
        assertEquals(2.0, LengthUnit.FEET.convertFromBaseUnit(2.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_FeetToInches() {
        assertEquals(12.0, LengthUnit.INCHES.convertFromBaseUnit(1.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_FeetToYards() {
        assertEquals(1.0, LengthUnit.YARDS.convertFromBaseUnit(3.0), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_FeetToCentimeters() {
        assertEquals(30.48, LengthUnit.CENTIMETERS.convertFromBaseUnit(1.0), 1e-3);
    }

    @Test
    void testRoundTripConversion_RefactoredDesign() {
        for (LengthUnit unit : LengthUnit.values()) {
            assertEquals(5.5, unit.convertFromBaseUnit(unit.convertToBaseUnit(5.5)), DELTA);
        }
    }

    @Test
    void testUnitImmutability() {
        double before = LengthUnit.YARDS.getConversionFactor();
        LengthUnit.YARDS.convertToBaseUnit(10.0);
        assertEquals(before, LengthUnit.YARDS.getConversionFactor(), DELTA);
    }

    @Test
    void testGetUnitName() {
        assertEquals("FEET", LengthUnit.FEET.getUnitName());
    }
}
