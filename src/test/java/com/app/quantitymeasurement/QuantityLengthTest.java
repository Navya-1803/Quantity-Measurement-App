package com.app.quantitymeasurement;

import com.app.quantitymeasurement.QuantityLength.LengthUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityLengthTest {

    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        assertEquals(new QuantityLength(12.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(2.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.INCHES), new QuantityLength(2.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InvalidUnit() {
        // only units defined in the enum are accepted
        assertThrows(IllegalArgumentException.class, () -> LengthUnit.valueOf("METER"));
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
    }

    @Test
    void testEquality_SameReference() {
        QuantityLength length = new QuantityLength(1.0, LengthUnit.FEET);
        assertEquals(length, length);
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(null, new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testHashCode_EquivalentValues() {
        assertEquals(new QuantityLength(1.0, LengthUnit.FEET).hashCode(), new QuantityLength(12.0, LengthUnit.INCHES).hashCode());
    }

    @Test
    void testToString_Format() {
        assertEquals("Quantity(1.0, FEET)", new QuantityLength(1.0, LengthUnit.FEET).toString());
    }

    @Test
    void testLengthUnit_ConversionFactors() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), 1e-9);
        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), 1e-9);
    }
}
