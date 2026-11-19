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

    // ---------- UC5: unit to unit conversion ----------

    private static final double DELTA = 1e-6;

    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES), DELTA);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0, QuantityLength.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET), DELTA);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0, QuantityLength.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES), DELTA);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(1.0, QuantityLength.convert(36.0, LengthUnit.INCHES, LengthUnit.YARDS), DELTA);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(0.393701, QuantityLength.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES), DELTA);
    }

    @Test
    void testConversion_FeatToYard() {
        assertEquals(2.0, QuantityLength.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS), DELTA);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double inches = QuantityLength.convert(7.5, LengthUnit.FEET, LengthUnit.INCHES);
        assertEquals(7.5, QuantityLength.convert(inches, LengthUnit.INCHES, LengthUnit.FEET), DELTA);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, QuantityLength.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES), DELTA);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0, QuantityLength.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES), DELTA);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(1.0, null, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(1.0, LengthUnit.FEET, null));
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES));
        assertThrows(IllegalArgumentException.class, () -> QuantityLength.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));
    }

    @Test
    void testConversion_PrecisionTolerance() {
        assertEquals(30.48, QuantityLength.convert(1.0, LengthUnit.FEET, LengthUnit.CENTIMETERS), 1e-3);
    }

    @Test
    void testConvertTo_ReturnsNewInstance() {
        QuantityLength feet = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength inches = feet.convertTo(LengthUnit.INCHES);
        assertEquals(LengthUnit.INCHES, inches.getUnit());
        assertEquals(12.0, inches.getValue(), DELTA);
        // the original object is unchanged
        assertEquals(LengthUnit.FEET, feet.getUnit());
    }

    // ---------- UC6: addition of two lengths ----------

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        assertEquals(new QuantityLength(3.0, LengthUnit.FEET),
                new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(2.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        assertEquals(new QuantityLength(12.0, LengthUnit.INCHES),
                new QuantityLength(6.0, LengthUnit.INCHES).add(new QuantityLength(6.0, LengthUnit.INCHES)));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        QuantityLength result = new QuantityLength(1.0, LengthUnit.FEET).add(new QuantityLength(12.0, LengthUnit.INCHES));
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        QuantityLength result = new QuantityLength(12.0, LengthUnit.INCHES).add(new QuantityLength(1.0, LengthUnit.FEET));
        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(24.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        assertEquals(2.0, new QuantityLength(1.0, LengthUnit.YARDS).add(new QuantityLength(3.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        assertEquals(5.08, new QuantityLength(2.54, LengthUnit.CENTIMETERS).add(new QuantityLength(1.0, LengthUnit.INCHES)).getValue(), 1e-3);
    }

    @Test
    void testAddition_Commutativity() {
        QuantityLength a = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength b = new QuantityLength(12.0, LengthUnit.INCHES);
        assertEquals(a.add(b), b.add(a));
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0, new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(0.0, LengthUnit.INCHES)).getValue(), DELTA);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0, new QuantityLength(5.0, LengthUnit.FEET).add(new QuantityLength(-2.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityLength(1.0, LengthUnit.FEET).add(null));
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2e6, new QuantityLength(1e6, LengthUnit.FEET).add(new QuantityLength(1e6, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_SmallValues() {
        assertEquals(0.003, new QuantityLength(0.001, LengthUnit.FEET).add(new QuantityLength(0.002, LengthUnit.FEET)).getValue(), DELTA);
    }
}
