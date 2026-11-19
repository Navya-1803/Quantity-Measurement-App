package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VolumeQuantityTest {

    private static final double DELTA = 1e-6;

    // ---------- equality ----------

    @Test
    void testEquality_LitreToLitre_SameValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE), new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertEquals(new Quantity<>(3.78541, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.GALLON));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertEquals(new Quantity<>(1.0, VolumeUnit.GALLON), new Quantity<>(3.78541, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, LengthUnit.FEET));
    }

    @Test
    void testEquality_VolumeVsWeight_Incompatible() {
        assertNotEquals(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testEquality_NullComparison() {
        assertNotEquals(null, new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals(litre, litre);
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<VolumeUnit>(1.0, null));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> gallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litre = new Quantity<>(3.78541, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(3785.41, VolumeUnit.MILLILITRE);
        assertTrue(gallon.equals(litre) && litre.equals(ml) && gallon.equals(ml));
    }

    @Test
    void testEquality_ZeroValue() {
        assertEquals(new Quantity<>(0.0, VolumeUnit.LITRE), new Quantity<>(0.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertEquals(new Quantity<>(-1.0, VolumeUnit.LITRE), new Quantity<>(-1000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertEquals(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE), new Quantity<>(1000.0, VolumeUnit.LITRE));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertEquals(new Quantity<>(0.001, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.MILLILITRE));
    }

    // ---------- conversion ----------

    @Test
    void testConversion_LitreToMillilitre() {
        assertEquals(1000.0, new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(), DELTA);
    }

    @Test
    void testConversion_MillilitreToLitre() {
        assertEquals(0.5, new Quantity<>(500.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.LITRE).getValue(), DELTA);
    }

    @Test
    void testConversion_GallonToLitre() {
        assertEquals(7.57082, new Quantity<>(2.0, VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE).getValue(), DELTA);
    }

    @Test
    void testConversion_LitreToGallon() {
        assertEquals(0.264172, new Quantity<>(1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.GALLON).getValue(), DELTA);
    }

    @Test
    void testConversion_MillilitreToGallon() {
        assertEquals(0.132086, new Quantity<>(500.0, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON).getValue(), DELTA);
    }

    @Test
    void testConversion_SameUnit() {
        assertEquals(4.0, new Quantity<>(4.0, VolumeUnit.GALLON).convertTo(VolumeUnit.GALLON).getValue(), DELTA);
    }

    @Test
    void testConversion_ZeroValue() {
        assertEquals(0.0, new Quantity<>(0.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(), DELTA);
    }

    @Test
    void testConversion_NegativeValue() {
        assertEquals(-1000.0, new Quantity<>(-1.0, VolumeUnit.LITRE).convertTo(VolumeUnit.MILLILITRE).getValue(), DELTA);
    }

    @Test
    void testConversion_RoundTrip() {
        Quantity<VolumeUnit> original = new Quantity<>(2.5, VolumeUnit.GALLON);
        assertEquals(original, original.convertTo(VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON));
    }

    // ---------- addition ----------

    @Test
    void testAddition_SameUnit_LitrePlusLitre() {
        assertEquals(new Quantity<>(3.0, VolumeUnit.LITRE), new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        assertEquals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(500.0, VolumeUnit.MILLILITRE).add(new Quantity<>(500.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));
        assertEquals(VolumeUnit.LITRE, result.getUnit());
        assertEquals(2.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        Quantity<VolumeUnit> result = new Quantity<>(1000.0, VolumeUnit.MILLILITRE).add(new Quantity<>(1.0, VolumeUnit.LITRE));
        assertEquals(VolumeUnit.MILLILITRE, result.getUnit());
        assertEquals(2000.0, result.getValue(), DELTA);
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        assertEquals(2.0, new Quantity<>(1.0, VolumeUnit.GALLON).add(new Quantity<>(3.78541, VolumeUnit.LITRE)).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        assertEquals(4.78541, new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(1.0, VolumeUnit.GALLON), VolumeUnit.LITRE).getValue(), DELTA);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        assertEquals(4785.41, new Quantity<>(1.0, VolumeUnit.LITRE).add(new Quantity<>(1.0, VolumeUnit.GALLON), VolumeUnit.MILLILITRE).getValue(), 1e-3);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        assertEquals(1.26417, new Quantity<>(1000.0, VolumeUnit.MILLILITRE).add(new Quantity<>(1.0, VolumeUnit.GALLON), VolumeUnit.GALLON).getValue(), 1e-5);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1.0, VolumeUnit.GALLON);
        assertEquals(a.add(b, VolumeUnit.LITRE), b.add(a, VolumeUnit.LITRE));
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0, new Quantity<>(5.0, VolumeUnit.LITRE).add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)).getValue(), DELTA);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0, new Quantity<>(5.0, VolumeUnit.LITRE).add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE)).getValue(), DELTA);
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2e6, new Quantity<>(1e6, VolumeUnit.LITRE).add(new Quantity<>(1e6, VolumeUnit.LITRE)).getValue(), DELTA);
    }

    @Test
    void testAddition_SmallValues() {
        assertEquals(0.003, new Quantity<>(1.0, VolumeUnit.MILLILITRE).add(new Quantity<>(2.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE).getValue(), DELTA);
    }

    // ---------- integration with the generic design ----------

    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals(litre, litre.convertTo(VolumeUnit.MILLILITRE));
        assertEquals(litre.add(litre), new Quantity<>(2000.0, VolumeUnit.MILLILITRE));
    }

    @Test
    void testScalability_VolumeIntegration() {
        // the app demonstration methods work for volume without any change
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
        assertEquals(new Quantity<>(2.0, VolumeUnit.LITRE), QuantityMeasurementApp.demonstrateAddition(
                new Quantity<>(1.0, VolumeUnit.LITRE), new Quantity<>(1000.0, VolumeUnit.MILLILITRE), VolumeUnit.LITRE));
    }
}
