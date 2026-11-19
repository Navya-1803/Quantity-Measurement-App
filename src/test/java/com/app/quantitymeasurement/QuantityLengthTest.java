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

    // ---------- UC4: yards and centimeters ----------

    @Test
    void testEquality_YardToYard_SameValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(2.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        assertEquals(new QuantityLength(3.0, LengthUnit.FEET), new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(36.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        assertEquals(new QuantityLength(36.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(2.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_centimetersToInches_EquivalentValue() {
        assertEquals(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(0.393701, LengthUnit.INCHES));
    }

    @Test
    void testEquality_centimetersToFeet_NonEquivalentValue() {
        assertNotEquals(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        QuantityLength feet = new QuantityLength(3.0, LengthUnit.FEET);
        QuantityLength inches = new QuantityLength(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(feet) && feet.equals(inches) && yard.equals(inches));
    }

    @Test
    void testEquality_YardWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, null));
    }

    @Test
    void testEquality_YardSameReference() {
        QuantityLength yard = new QuantityLength(1.0, LengthUnit.YARDS);
        assertEquals(yard, yard);
    }

    @Test
    void testEquality_YardNullComparison() {
        assertNotEquals(null, new QuantityLength(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(2.0, null));
    }

    @Test
    void testEquality_CentimetersSameReference() {
        QuantityLength cm = new QuantityLength(2.0, LengthUnit.CENTIMETERS);
        assertEquals(cm, cm);
    }

    @Test
    void testEquality_CentimetersNullComparison() {
        assertNotEquals(null, new QuantityLength(2.0, LengthUnit.CENTIMETERS));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        // 2 yards = 6 feet = 72 inches = 182.88 cm
        QuantityLength yards = new QuantityLength(2.0, LengthUnit.YARDS);
        assertEquals(yards, new QuantityLength(6.0, LengthUnit.FEET));
        assertEquals(yards, new QuantityLength(72.0, LengthUnit.INCHES));
        assertEquals(yards, new QuantityLength(72.0 / 0.393701, LengthUnit.CENTIMETERS));
    }
}
