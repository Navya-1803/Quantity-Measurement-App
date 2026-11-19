package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityArithmeticTest {

    private static final double DELTA = 1e-6;

    // ---------- subtraction ----------

    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        assertEquals(new Quantity<>(5.0, LengthUnit.FEET), new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        assertEquals(new Quantity<>(3.0, VolumeUnit.LITRE), new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES));
        assertEquals(LengthUnit.FEET, result.getUnit());
        assertEquals(9.5, result.getValue(), DELTA);
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        assertEquals(60.0, new Quantity<>(120.0, LengthUnit.INCHES).subtract(new Quantity<>(5.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        assertEquals(9.5, new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.FEET).getValue(), DELTA);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        assertEquals(114.0, new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES).getValue(), DELTA);
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        assertEquals(3000.0, new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE).getValue(), DELTA);
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        assertEquals(-5.0, new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(10.0, LengthUnit.FEET)).getValue(), DELTA);
        assertEquals(-3.0, new Quantity<>(2.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5.0, WeightUnit.KILOGRAM)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_ResultingInZero() {
        assertEquals(0.0, new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(120.0, LengthUnit.INCHES)).getValue(), DELTA);
        assertEquals(0.0, new Quantity<>(1.0, VolumeUnit.LITRE).subtract(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        assertEquals(5.0, new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(0.0, LengthUnit.INCHES)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        assertEquals(7.0, new Quantity<>(5.0, LengthUnit.FEET).subtract(new Quantity<>(-2.0, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        assertNotEquals(a.subtract(b), b.subtract(a));
    }

    @Test
    void testSubtraction_WithLargeValues() {
        assertEquals(5e5, new Quantity<>(1e6, WeightUnit.KILOGRAM).subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_WithSmallValues() {
        assertEquals(0.01, new Quantity<>(0.02, LengthUnit.FEET).subtract(new Quantity<>(0.01, LengthUnit.FEET)).getValue(), DELTA);
    }

    @Test
    void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
    }

    @Test
    void testSubtraction_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(5.0, LengthUnit.FEET), null));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void testSubtraction_CrossCategory() {
        Quantity feet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity kilogram = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> feet.subtract(kilogram));
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        assertEquals(new Quantity<>(9.5, LengthUnit.FEET), new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES)));
        assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5000.0, WeightUnit.GRAM)));
        assertEquals(new Quantity<>(4.5, VolumeUnit.LITRE), new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(500.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testSubtraction_ChainedOperations() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(12.0, LengthUnit.INCHES));
        assertEquals(7.0, result.getValue(), DELTA);
    }

    // ---------- division ----------

    @Test
    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(0.5, new Quantity<>(5.0, VolumeUnit.LITRE).divide(new Quantity<>(10.0, VolumeUnit.LITRE)), DELTA);
    }

    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        assertEquals(1.0, new Quantity<>(1.0, LengthUnit.FEET).divide(new Quantity<>(12.0, LengthUnit.INCHES)), DELTA);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        assertEquals(2.0, new Quantity<>(2.0, WeightUnit.KILOGRAM).divide(new Quantity<>(1000.0, WeightUnit.GRAM)), DELTA);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        assertTrue(new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)) > 1.0);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        assertTrue(new Quantity<>(2.0, LengthUnit.FEET).divide(new Quantity<>(10.0, LengthUnit.FEET)) < 1.0);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0, new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testDivision_NonCommutative() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(5.0, LengthUnit.FEET);
        assertNotEquals(a.divide(b), b.divide(a), DELTA);
    }

    @Test
    void testDivision_ByZero() {
        assertThrows(ArithmeticException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(0.0, LengthUnit.FEET)));
    }

    @Test
    void testDivision_WithLargeRatio() {
        assertEquals(1e6, new Quantity<>(1e6, WeightUnit.KILOGRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), DELTA);
    }

    @Test
    void testDivision_WithSmallRatio() {
        assertEquals(1e-6, new Quantity<>(1.0, WeightUnit.GRAM).divide(new Quantity<>(1000.0, WeightUnit.KILOGRAM)), 1e-9);
    }

    @Test
    void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void testDivision_CrossCategory() {
        Quantity feet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity litre = new Quantity<>(5.0, VolumeUnit.LITRE);
        assertThrows(IllegalArgumentException.class, () -> feet.divide(litre));
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertEquals(1.0, new Quantity<>(12.0, LengthUnit.INCHES).divide(new Quantity<>(1.0, LengthUnit.FEET)), DELTA);
        assertEquals(2.0, new Quantity<>(2000.0, WeightUnit.GRAM).divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), DELTA);
        assertEquals(1.0, new Quantity<>(1000.0, VolumeUnit.MILLILITRE).divide(new Quantity<>(1.0, VolumeUnit.LITRE)), DELTA);
    }

    @Test
    void testDivision_Associativity() {
        // (a / b) / c is not equal to a / (b / c), division is not associative
        Quantity<LengthUnit> a = new Quantity<>(8.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(4.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(2.0, LengthUnit.FEET);
        double left = a.divide(b) / c.getValue();
        double right = a.getValue() / b.divide(c);
        assertNotEquals(left, right, DELTA);
    }

    // ---------- integration ----------

    @Test
    void testSubtractionAndDivision_Integration() {
        Quantity<LengthUnit> difference = new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(4.0, LengthUnit.FEET));
        assertEquals(3.0, difference.divide(new Quantity<>(2.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        Quantity<WeightUnit> start = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> other = new Quantity<>(1500.0, WeightUnit.GRAM);
        assertEquals(start, start.add(other).subtract(other));
    }

    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(6.0, LengthUnit.INCHES);
        a.subtract(b);
        assertEquals(10.0, a.getValue(), DELTA);
        assertEquals(6.0, b.getValue(), DELTA);
    }

    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> a = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(2.0, LengthUnit.FEET);
        a.divide(b);
        assertEquals(10.0, a.getValue(), DELTA);
        assertEquals(2.0, b.getValue(), DELTA);
    }

    @Test
    void testSubtraction_PrecisionAndRounding() {
        // 1 foot - 1 inch = 0.9166.. feet, rounded to 0.92
        assertEquals(0.92, new Quantity<>(1.0, LengthUnit.FEET).subtract(new Quantity<>(1.0, LengthUnit.INCHES)).getValue(), DELTA);
    }

    @Test
    void testDivision_PrecisionHandling() {
        // division is not rounded
        assertEquals(1.0 / 3.0, new Quantity<>(1.0, LengthUnit.FEET).divide(new Quantity<>(3.0, LengthUnit.FEET)), DELTA);
    }
}
