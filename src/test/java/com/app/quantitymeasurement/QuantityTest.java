package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    private static final double DELTA = 1e-6;

    // ---------- UC10: generic quantity ----------

    @Test
    void testIMeasurableInterface_LengthUnitImplementation() {
        IMeasurable unit = LengthUnit.INCHES;
        assertEquals(1.0, unit.convertToBaseUnit(12.0), DELTA);
        assertEquals("INCHES", unit.getUnitName());
    }

    @Test
    void testIMeasurableInterface_WeightUnitImplementation() {
        IMeasurable unit = WeightUnit.GRAM;
        assertEquals(1.0, unit.convertToBaseUnit(1000.0), DELTA);
        assertEquals("GRAM", unit.getUnitName());
    }

    @Test
    void testIMeasurableInterface_ConsistentBehavior() {
        IMeasurable[] units = {LengthUnit.YARDS, WeightUnit.POUND};
        for (IMeasurable unit : units) {
            assertEquals(unit.getConversionFactor(), unit.convertToBaseUnit(1.0), DELTA);
            assertEquals(1.0, unit.convertFromBaseUnit(unit.getConversionFactor()), DELTA);
        }
    }

    @Test
    void testGenericQuantity_LengthOperations_Equality() {
        assertEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES));
    }

    @Test
    void testGenericQuantity_WeightOperations_Equality() {
        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM));
    }

    @Test
    void testGenericQuantity_LengthOperations_Conversion() {
        assertEquals(new Quantity<>(12.0, LengthUnit.INCHES), new Quantity<>(1.0, LengthUnit.FEET).convertTo(LengthUnit.INCHES));
    }

    @Test
    void testGenericQuantity_WeightOperations_Conversion() {
        assertEquals(new Quantity<>(1000.0, WeightUnit.GRAM), new Quantity<>(1.0, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM));
    }

    @Test
    void testGenericQuantity_LengthOperations_Addition() {
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET),
                new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET));
    }

    @Test
    void testGenericQuantity_WeightOperations_Addition() {
        assertEquals(new Quantity<>(2.0, WeightUnit.KILOGRAM),
                new Quantity<>(1.0, WeightUnit.KILOGRAM).add(new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM));
    }

    @Test
    void testCrossCategoryPrevention_LengthVsWeight() {
        assertNotEquals(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(1.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testGenericQuantity_ConstructorValidation_NullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<LengthUnit>(1.0, null));
    }

    @Test
    void testGenericQuantity_ConstructorValidation_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, WeightUnit.GRAM));
    }

    @Test
    void testGenericQuantity_Conversion_AllUnitCombinations() {
        for (LengthUnit from : LengthUnit.values()) {
            for (LengthUnit to : LengthUnit.values()) {
                Quantity<LengthUnit> original = new Quantity<>(4.0, from);
                assertEquals(original, original.convertTo(to));
            }
        }
        for (WeightUnit from : WeightUnit.values()) {
            for (WeightUnit to : WeightUnit.values()) {
                Quantity<WeightUnit> original = new Quantity<>(4.0, from);
                assertEquals(original, original.convertTo(to));
            }
        }
    }

    @Test
    void testHashCode_GenericQuantity_Consistency() {
        assertEquals(new Quantity<>(1.0, WeightUnit.KILOGRAM).hashCode(), new Quantity<>(1000.0, WeightUnit.GRAM).hashCode());
    }

    @Test
    void testEquals_GenericQuantity_ContractPreservation() {
        Quantity<LengthUnit> a = new Quantity<>(1.0, LengthUnit.YARDS);
        Quantity<LengthUnit> b = new Quantity<>(3.0, LengthUnit.FEET);
        Quantity<LengthUnit> c = new Quantity<>(36.0, LengthUnit.INCHES);
        assertEquals(a, a);
        assertTrue(a.equals(b) && b.equals(a));
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c));
        assertNotEquals(null, a);
    }

    @Test
    void testImmutability_GenericQuantity() {
        Quantity<LengthUnit> original = new Quantity<>(1.0, LengthUnit.FEET);
        original.add(new Quantity<>(1.0, LengthUnit.FEET));
        original.convertTo(LengthUnit.INCHES);
        assertEquals(1.0, original.getValue(), DELTA);
        assertEquals(LengthUnit.FEET, original.getUnit());
    }

    @Test
    void testToString_GenericQuantity() {
        assertEquals("Quantity(2.0, POUND)", new Quantity<>(2.0, WeightUnit.POUND).toString());
    }
}
