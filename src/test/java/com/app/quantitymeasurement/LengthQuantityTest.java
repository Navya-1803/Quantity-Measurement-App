package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LengthQuantityTest {

    private static final double DELTA = 1e-6;

    @Test
    void testEquality_FeetToFeet_SameValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_SameValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_FeetToInch_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InchToFeet_EquivalentValue() {
        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_FeetToFeet_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(2.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InchToInch_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.INCHES), new Quantity<>(2.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InvalidUnit() {
        // only units defined in the enum are accepted
        assertThrows(IllegalArgumentException.class, () -> LengthUnit.valueOf("METER"));
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(1.0, null));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<LengthUnit> length = new Quantity<>(1.0, LengthUnit.FEET);
        assertEquals(length, length);
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(null, new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
    }

    @Test
    void testHashCode_EquivalentValues() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET).hashCode(), new Quantity<>(12.0, LengthUnit.INCHES).hashCode());
    }

    @Test
    void testToString_Format() {
        assertEquals("Quantity(1.0, FEET)", new Quantity<>(1.0, LengthUnit.FEET).toString());
    }

    @Test
    void testLengthUnit_ConversionFactors() {
        assertEquals(1.0, LengthUnit.FEET.getConversionFactor(), 1e-9);
        assertEquals(1.0 / 12.0, LengthUnit.INCHES.getConversionFactor(), 1e-9);
    }

    // ---------- UC4: yards and centimeters ----------

    @Test
    void testEquality_YardToYard_SameValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToYard_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(2.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToFeet_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(3.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_FeetToYard_EquivalentValue() {
        assertEquals(new Quantity<>(3.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToInches_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(36.0, LengthUnit.INCHES));
    }

    @Test
    void testEquality_InchesToYard_EquivalentValue() {
        assertEquals(new Quantity<>(36.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_YardToFeet_NonEquivalentValue() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(2.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_centimetersToInches_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, LengthUnit.CENTIMETERS), new Quantity<>(0.393701, LengthUnit.INCHES));
    }

    @Test
    void testEquality_centimetersToFeet_NonEquivalentValue() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.CENTIMETERS), new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_MultiUnit_TransitiveProperty() {
        Quantity<LengthUnit> yard = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> feet = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = new Quantity<>(36.0, LengthUnit.INCHES);
        assertTrue(yard.equals(feet) && feet.equals(inches) && yard.equals(inches));
    }

    @Test
    void testEquality_YardWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(1.0, null));
    }

    @Test
    void testEquality_YardSameReference() {
        Quantity<LengthUnit> yard = new Quantity<>(1.0, LengthUnit.YARDS);
        assertEquals(yard, yard);
    }

    @Test
    void testEquality_YardNullComparison() {
        assertNotEquals(null, new Quantity<>(1.0, LengthUnit.YARDS));
    }

    @Test
    void testEquality_CentimetersWithNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(2.0, null));
    }

    @Test
    void testEquality_CentimetersSameReference() {
        Quantity<LengthUnit> cm = new Quantity<>(2.0, LengthUnit.CENTIMETERS);
        assertEquals(cm, cm);
    }

    @Test
    void testEquality_CentimetersNullComparison() {
        assertNotEquals(null, new Quantity<>(2.0, LengthUnit.CENTIMETERS));
    }

    @Test
    void testEquality_AllUnits_ComplexScenario() {
        // 2 yards = 6 feet = 72 inches = 182.88 cm
        Quantity<LengthUnit> yards = new Quantity<>(2.0, LengthUnit.YARDS);
        assertEquals(yards, new Quantity<>(6.0, LengthUnit.FEET));
        assertEquals(yards, new Quantity<>(72.0, LengthUnit.INCHES));
        assertEquals(yards, new Quantity<>(72.0 / 0.393701, LengthUnit.CENTIMETERS));
    }

    // ---------- UC5: unit to unit conversion ----------


    @Test
    void testConversion_FeetToInches() {
        assertEquals(12.0, new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testConversion_InchesToFeet() {
        assertEquals(2.0, new Quantity<>(24.0, LengthUnit.INCHES).convertTo(LengthUnit.FEET).getValue(), DELTA);
    }

    @Test
    void testConversion_YardsToInches() {
        assertEquals(36.0, new Quantity<>(1.0, LengthUnit.YARDS).convertTo(LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testConversion_InchesToYards() {
        assertEquals(1.0, new Quantity<>(36.0, LengthUnit.INCHES).convertTo(LengthUnit.YARDS).getValue(), DELTA);
    }

    @Test
    void testConversion_CentimetersToInches() {
        assertEquals(0.393701, new Quantity<>(1.0, LengthUnit.CENTIMETERS).convertTo(LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testConversion_FeatToYard() {
        assertEquals(2.0, new Quantity<>(6.0, LengthUnit.FEET).convertTo(LengthUnit.YARDS).getValue(), DELTA);
    }

    @Test
    void testConversion_RoundTrip_PreservesValue() {
        double inches = new Quantity<>(7.5, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue();
        assertEquals(7.5, new Quantity<>(inches, LengthUnit.INCHES).convertTo(LengthUnit.FEET).getValue(), DELTA);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, new Quantity<>(0.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-12.0, new Quantity<>(-1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testConversion_InvalidUnit_Throws() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(1.0, null).convertTo(LengthUnit.FEET).getValue());
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, LengthUnit.FEET).convertTo(null).getValue());
    }

    @Test
    void testConversion_NaNOrInfinite_Throws() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue());
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET).convertTo(LengthUnit.INCHES).getValue());
    }

    @Test
    void testConversion_PrecisionTolerance() {
        assertEquals(30.48, new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.CENTIMETERS).getValue(), 1e-3);
    }

    @Test
    void testConvertTo_ReturnsNewInstance() {
        Quantity<LengthUnit> feet = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> inches = feet.convertTo(LengthUnit.INCHES);
        assertEquals(LengthUnit.INCHES, inches.getUnit());
        assertEquals(12.0, inches.getValue(), DELTA);
        // the original object is unchanged
        assertEquals(LengthUnit.FEET, feet.getUnit());
    }

    // ---------- UC6: addition of two lengths ----------

    @Test
    void testAddition_SameUnit_FeetPlusFeet() {
        assertEquals(new Quantity<>(3.0, LengthUnit.FEET),
                new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(2.0, LengthUnit.FEET)));
    }

    @Test
    void testAddition_SameUnit_InchPlusInch() {
        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES),
                new Quantity<>(6.0, LengthUnit.INCHES).add(new Quantity<>(6.0, LengthUnit.INCHES)));
    }

    @Test
    void testAddition_CrossUnit_FeetPlusInches() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES));
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_InchPlusFeet() {
        Quantity<LengthUnit> result = new Quantity<>(12.0, LengthUnit.INCHES).add(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(24.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_YardPlusFeet() {
        assertEquals(2.0, new Quantity<>(1.0, LengthUnit.YARDS).add(new Quantity<>(3.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_CentimeterPlusInch() {
        assertEquals(5.08, new Quantity<>(2.54, LengthUnit.CENTIMETERS).add(new Quantity<>(1.0, LengthUnit.INCHES)).getValue(), 1e-3);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(12.0, LengthUnit.INCHES);
        assertEquals(a.add(b), b.add(a));
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0, new Quantity<>(5.0, LengthUnit.FEET).add(new Quantity<>(0.0, LengthUnit.INCHES)).getValue(), DELTA);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0, new Quantity<>(5.0, LengthUnit.FEET).add(new Quantity<>(-2.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_NullSecondOperand() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, LengthUnit.FEET).add(null));
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2e6, new Quantity<>(1e6, LengthUnit.FEET).add(new Quantity<>(1e6, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testAddition_SmallValues() {
        assertEquals(0.03, new Quantity<>(0.01, LengthUnit.FEET).add(new Quantity<>(0.02, LengthUnit.FEET)).getValue(), DELTA);
    }

    // ---------- UC7: addition with target unit ----------

    @Test
    void testAddition_ExplicitTargetUnit_Feet() {
        Quantity<LengthUnit> result = new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Inches() {
        assertEquals(24.0, new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Yards() {
        assertEquals(0.67, new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.YARDS).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Centimeters() {
        assertEquals(5.08, new Quantity<>(2.54, LengthUnit.CENTIMETERS).add(new Quantity<>(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS).getValue(), 1e-3);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {
        Quantity<LengthUnit> a = new Quantity<>(2.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(6.0, LengthUnit.INCHES);
        assertEquals(a.add(b), a.add(b, LengthUnit.FEET));
    }

    @Test
    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {
        Quantity<LengthUnit> result = new Quantity<>(2.0, LengthUnit.FEET).add(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);
        assertEquals(30.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Commutativity() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        assertEquals(a.add(b, LengthUnit.INCHES), b.add(a, LengthUnit.INCHES));
    }

    @Test
    void testAddition_ExplicitTargetUnit_WithZero() {
        assertEquals(1.67, new Quantity<>(5.0, LengthUnit.FEET).add(new Quantity<>(0.0, LengthUnit.INCHES), LengthUnit.YARDS).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NegativeValues() {
        assertEquals(36.0, new Quantity<>(5.0, LengthUnit.FEET).add(new Quantity<>(-2.0, LengthUnit.FEET), LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), null));
    }

    @Test
    void testAddition_ExplicitTargetUnit_LargeToSmallScale() {
        assertEquals(18000.0, new Quantity<>(1000.0, LengthUnit.FEET).add(new Quantity<>(500.0, LengthUnit.FEET), LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_SmallToLargeScale() {
        assertEquals(0.67, new Quantity<>(12.0, LengthUnit.INCHES).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.YARDS).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_AllUnitCombinations() {
        for (LengthUnit first : LengthUnit.values()) {
            for (LengthUnit second : LengthUnit.values()) {
                for (LengthUnit target : LengthUnit.values()) {
                    Quantity<LengthUnit> a = new Quantity<>(1.0, first);
                    Quantity<LengthUnit> b = new Quantity<>(2.0, second);
                    double expectedFeet = first.getConversionFactor() + 2.0 * second.getConversionFactor();
                    double actual = a.add(b, target).getValue();
                    // results are rounded to two decimal places since UC13
                    assertEquals(expectedFeet / target.getConversionFactor(), actual, 0.005);
                }
            }
        }
    }

    @Test
    void testAddition_ExplicitTargetUnit_PrecisionTolerance() {
        Quantity<LengthUnit> sum = new Quantity<>(0.1, LengthUnit.FEET)
                .add(new Quantity<>(0.2, LengthUnit.FEET), LengthUnit.FEET);
        assertEquals(new Quantity<>(0.3, LengthUnit.FEET), sum);
    }

    // ---------- UC8: refactored design using standalone LengthUnit ----------

    @Test
    void testQuantityLengthRefactored_Equality() {
        assertEquals(new Quantity<>(36.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.YARDS));
    }

    @Test
    void testQuantityLengthRefactored_ConvertTo() {
        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES));
    }

    @Test
    void testQuantityLengthRefactored_Add() {
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testQuantityLengthRefactored_AddWithTargetUnit() {
        assertEquals(new Quantity<>(2.0, LengthUnit.YARDS),
                new Quantity<>(1.0, LengthUnit.YARDS).add(new Quantity<>(3.0, LengthUnit.FEET), LengthUnit.YARDS));
    }

    @Test
    void testQuantityLengthRefactored_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(1.0, null));
    }

    @Test
    void testQuantityLengthRefactored_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NEGATIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    void testQuantityLengthRefactored_ConvertToNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(1.0, LengthUnit.FEET).convertTo(null));
    }
}
