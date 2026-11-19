package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IMeasurableTest {

    private static final double DELTA = 1e-6;

    @Test
    void testAllUnits_ImplementIMeasurable() {
        assertInstanceOf(IMeasurable.class, LengthUnit.FEET);
        assertInstanceOf(IMeasurable.class, WeightUnit.KILOGRAM);
    }

    @Test
    void testRoundTrip_AllUnits() {
        for (IMeasurable unit : LengthUnit.values()) {
            assertEquals(9.0, unit.convertFromBaseUnit(unit.convertToBaseUnit(9.0)), DELTA);
        }
        for (IMeasurable unit : WeightUnit.values()) {
            assertEquals(9.0, unit.convertFromBaseUnit(unit.convertToBaseUnit(9.0)), DELTA);
        }
    }

    @Test
    void testGetUnitName_MatchesEnumName() {
        assertEquals("CENTIMETERS", LengthUnit.CENTIMETERS.getUnitName());
        assertEquals("POUND", WeightUnit.POUND.getUnitName());
    }
}
