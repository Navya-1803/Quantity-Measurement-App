package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityWeightTest {

    private static final double DELTA = 1e-6;

    // ---------- equality ----------

    @Test
    void testEquality_KilogramToKilogram_SameValue() {
        assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_KilogramToKilogram_DifferentValue() {
        assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(2.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_KilogramToGram_EquivalentValue() {
        assertEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM));
    }

    @Test
    void testEquality_GramToKilogram_EquivalentValue() {
        assertEquals(new QuantityWeight(1000.0, WeightUnit.GRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_PoundToGram_EquivalentValue() {
        assertEquals(new QuantityWeight(1.0, WeightUnit.POUND), new QuantityWeight(453.592, WeightUnit.GRAM));
    }

    @Test
    void testEquality_WeightVsLength_Incompatible() {
        assertNotEquals(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityLength(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(null, new QuantityWeight(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_SameReference() {
        QuantityWeight weight = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        assertEquals(weight, weight);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(1.0, null));
    }

    @Test
    void testEquality_TransitiveProperty() {
        QuantityWeight kg = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight g = new QuantityWeight(1000.0, WeightUnit.GRAM);
        QuantityWeight lb = new QuantityWeight(1.0 / 0.453592, WeightUnit.POUND);
        assertTrue(kg.equals(g) && g.equals(lb) && kg.equals(lb));
    }

    @Test
    void testEquality_ZeroValue() {
        assertEquals(new QuantityWeight(0.0, WeightUnit.KILOGRAM), new QuantityWeight(0.0, WeightUnit.GRAM));
    }

    @Test
    void testEquality_NegativeWeight() {
        assertEquals(new QuantityWeight(-1.0, WeightUnit.KILOGRAM), new QuantityWeight(-1000.0, WeightUnit.GRAM));
    }

    @Test
    void testEquality_LargeWeightValue() {
        assertEquals(new QuantityWeight(1000000.0, WeightUnit.GRAM), new QuantityWeight(1000.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_SmallWeightValue() {
        assertEquals(new QuantityWeight(0.001, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.GRAM));
    }

    @Test
    void testEquality_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(Double.NaN, WeightUnit.GRAM));
    }

    // ---------- conversion ----------

    @Test
    void testConversion_PoundToKilogram() {
        assertEquals(0.907184, new QuantityWeight(2.0, WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM).getValue(), DELTA);
    }

    @Test
    void testConversion_KilogramToPound() {
        assertEquals(2.20462, new QuantityWeight(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.POUND).getValue(), 1e-5);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(5.0, new QuantityWeight(5.0, WeightUnit.GRAM).convertTo(WeightUnit.GRAM).getValue(), DELTA);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, new QuantityWeight(0.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).getValue(), DELTA);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0, new QuantityWeight(-1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM).getValue(), DELTA);
    }

    @Test
    void testConversion_RoundTrip() {
        QuantityWeight original = new QuantityWeight(1.5, WeightUnit.KILOGRAM);
        assertEquals(original, original.convertTo(WeightUnit.POUND).convertTo(WeightUnit.KILOGRAM));
    }

    @Test
    void testConversion_StaticConvert() {
        assertEquals(1000.0, QuantityWeight.convert(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM), DELTA);
    }

    // ---------- addition ----------

    @Test
    void testAddition_SameUnit_KilogramPlusKilogram() {
        assertEquals(new QuantityWeight(3.0, WeightUnit.KILOGRAM),
                new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(2.0, WeightUnit.KILOGRAM)));
    }

    @Test
    void testAddition_CrossUnit_KilogramPlusGram() {
        QuantityWeight result = new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1000.0, WeightUnit.GRAM));
        assertEquals(WeightUnit.KILOGRAM, result.getUnit());
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_PoundPlusKilogram() {
        QuantityWeight result = new QuantityWeight(1.0, WeightUnit.POUND).add(new QuantityWeight(0.453592, WeightUnit.KILOGRAM));
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Kilogram() {
        QuantityWeight result = new QuantityWeight(2.0, WeightUnit.KILOGRAM).add(new QuantityWeight(4.0, WeightUnit.POUND), WeightUnit.KILOGRAM);
        assertEquals(3.814368, result.getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gram() {
        assertEquals(2000.0, new QuantityWeight(1.0, WeightUnit.KILOGRAM)
                .add(new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM).getValue(), DELTA);
    }

    @Test
    void testAddition_Commutativity() {
        QuantityWeight a = new QuantityWeight(1.0, WeightUnit.KILOGRAM);
        QuantityWeight b = new QuantityWeight(500.0, WeightUnit.GRAM);
        assertEquals(a.add(b), b.add(a));
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0, new QuantityWeight(5.0, WeightUnit.KILOGRAM).add(new QuantityWeight(0.0, WeightUnit.GRAM)).getValue(), DELTA);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0, new QuantityWeight(5.0, WeightUnit.KILOGRAM).add(new QuantityWeight(-2000.0, WeightUnit.GRAM)).getValue(), DELTA);
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2e6, new QuantityWeight(1e6, WeightUnit.KILOGRAM).add(new QuantityWeight(1e6, WeightUnit.KILOGRAM)).getValue(), DELTA);
    }

    @Test
    void testAddition_NullOperand() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(null));
    }

    @Test
    void testAddition_NullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new QuantityWeight(1.0, WeightUnit.KILOGRAM).add(new QuantityWeight(1.0, WeightUnit.GRAM), null));
    }

    @Test
    void testToString_Format() {
        assertEquals("Quantity(1.0, KILOGRAM)", new QuantityWeight(1.0, WeightUnit.KILOGRAM).toString());
    }
}
