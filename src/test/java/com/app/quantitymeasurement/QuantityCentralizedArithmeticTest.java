package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

class QuantityCentralizedArithmeticTest {

    private static final double DELTA = 1e-6;

    @Test
    void testRefactoring_Add_DelegatesViaHelper() {
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES)));
    }

    @Test
    void testRefactoring_Subtract_DelegatesViaHelper() {
        assertEquals(new Quantity<>(9.5, LengthUnit.FEET), new Quantity<>(10.0, LengthUnit.FEET).subtract(new Quantity<>(6.0, LengthUnit.INCHES)));
    }

    @Test
    void testRefactoring_Divide_DelegatesViaHelper() {
        assertEquals(5.0, new Quantity<>(10.0, LengthUnit.FEET).divide(new Quantity<>(2.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {
        Quantity<LengthUnit> feet = new Quantity<>(10.0, LengthUnit.FEET);
        String addMessage = assertThrows(IllegalArgumentException.class, () -> feet.add(null)).getMessage();
        String subtractMessage = assertThrows(IllegalArgumentException.class, () -> feet.subtract(null)).getMessage();
        String divideMessage = assertThrows(IllegalArgumentException.class, () -> feet.divide(null)).getMessage();
        assertEquals(addMessage, subtractMessage);
        assertEquals(addMessage, divideMessage);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations() {
        Quantity feet = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity kilogram = new Quantity<>(5.0, WeightUnit.KILOGRAM);
        assertThrows(IllegalArgumentException.class, () -> feet.add(kilogram));
        assertThrows(IllegalArgumentException.class, () -> feet.subtract(kilogram));
        assertThrows(IllegalArgumentException.class, () -> feet.divide(kilogram));
    }

    @Test
    void testValidation_FiniteValue_ConsistentAcrossOperations() {
        // non finite values can never reach the arithmetic helper, the constructor already rejects them
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.NaN, LengthUnit.FEET));
        assertThrows(IllegalArgumentException.class, () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    void testValidation_NullTargetUnit_AddSubtractReject() {
        Quantity<LengthUnit> feet = new Quantity<>(10.0, LengthUnit.FEET);
        assertThrows(IllegalArgumentException.class, () -> feet.add(feet, null));
        assertThrows(IllegalArgumentException.class, () -> feet.subtract(feet, null));
    }

    @Test
    void testPerformBaseArithmetic_ConversionAndOperation() {
        // 10 kg + 5000 g in grams = 15000 g
        assertEquals(15000.0, new Quantity<>(10.0, WeightUnit.KILOGRAM).add(new Quantity<>(5000.0, WeightUnit.GRAM), WeightUnit.GRAM).getValue(), DELTA);
    }

    @Test
    void testAdd_UC12_BehaviorPreserved() {
        assertEquals(new Quantity<>(15000.0, WeightUnit.GRAM),
                new Quantity<>(10.0, WeightUnit.KILOGRAM).add(new Quantity<>(5000.0, WeightUnit.GRAM), WeightUnit.GRAM));
    }

    @Test
    void testSubtract_UC12_BehaviorPreserved() {
        assertEquals(new Quantity<>(3000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(5.0, VolumeUnit.LITRE).subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE));
    }

    @Test
    void testDivide_UC12_BehaviorPreserved() {
        assertEquals(1.0, new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        assertEquals(0.67, new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.YARDS).getValue(), DELTA);
        assertEquals(0.92, new Quantity<>(1.0, LengthUnit.FEET).subtract(new Quantity<>(1.0, LengthUnit.INCHES)).getValue(), DELTA);
    }

    @Test
    void testRounding_Divide_NoRounding() {
        assertEquals(1.0 / 3.0, new Quantity<>(1.0, LengthUnit.FEET).divide(new Quantity<>(3.0, LengthUnit.FEET)), 1e-12);
    }

    @Test
    void testImplicitTargetUnit_AddSubtract() {
        assertEquals(LengthUnit.INCHES, new Quantity<>(12.0, LengthUnit.INCHES).add(new Quantity<>(1.0, LengthUnit.FEET)).getUnit());
        assertEquals(LengthUnit.INCHES, new Quantity<>(12.0, LengthUnit.INCHES).subtract(new Quantity<>(1.0, LengthUnit.FEET)).getUnit());
    }

    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides() {
        assertEquals(LengthUnit.YARDS, new Quantity<>(12.0, LengthUnit.INCHES).add(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.YARDS).getUnit());
        assertEquals(LengthUnit.FEET, new Quantity<>(36.0, LengthUnit.INCHES).subtract(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.FEET).getUnit());
    }

    @Test
    void testImmutability_AfterAdd_ViaCentralizedHelper() {
        Quantity<LengthUnit> original = new Quantity<>(1.0, LengthUnit.FEET);
        original.add(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(1.0, original.getValue(), DELTA);
    }

    @Test
    void testImmutability_AfterSubtract_ViaCentralizedHelper() {
        Quantity<LengthUnit> original = new Quantity<>(5.0, LengthUnit.FEET);
        original.subtract(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(5.0, original.getValue(), DELTA);
    }

    @Test
    void testImmutability_AfterDivide_ViaCentralizedHelper() {
        Quantity<LengthUnit> original = new Quantity<>(5.0, LengthUnit.FEET);
        original.divide(new Quantity<>(1.0, LengthUnit.FEET));
        assertEquals(5.0, original.getValue(), DELTA);
    }

    @Test
    void testAllOperations_AcrossAllCategories() {
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(1.0, LengthUnit.FEET)));
        assertEquals(new Quantity<>(5.0, WeightUnit.KILOGRAM), new Quantity<>(10.0, WeightUnit.KILOGRAM).subtract(new Quantity<>(5000.0, WeightUnit.GRAM)));
        assertEquals(1.0, new Quantity<>(1000.0, VolumeUnit.MILLILITRE).divide(new Quantity<>(1.0, VolumeUnit.LITRE)), DELTA);
    }

    @Test
    void testErrorMessage_Consistency_Across_Operations() {
        Quantity<VolumeUnit> litre = new Quantity<>(1.0, VolumeUnit.LITRE);
        assertEquals("Operand quantity cannot be null", assertThrows(IllegalArgumentException.class, () -> litre.add(null)).getMessage());
        assertEquals("Operand quantity cannot be null", assertThrows(IllegalArgumentException.class, () -> litre.divide(null)).getMessage());
    }

    @Test
    void testHelper_PrivateVisibility() throws NoSuchMethodException {
        Method helper = Quantity.class.getDeclaredMethod("performBaseArithmetic", Quantity.class, ArithmeticOperation.class);
        assertTrue(Modifier.isPrivate(helper.getModifiers()));
    }

    @Test
    void testValidation_Helper_PrivateVisibility() throws NoSuchMethodException {
        Method helper = Quantity.class.getDeclaredMethod("validateArithmeticOperands", Quantity.class, IMeasurable.class, boolean.class);
        assertTrue(Modifier.isPrivate(helper.getModifiers()));
    }

    @Test
    void testArithmetic_Chain_Operations() {
        Quantity<LengthUnit> result = new Quantity<>(10.0, LengthUnit.FEET)
                .add(new Quantity<>(2.0, LengthUnit.FEET))
                .subtract(new Quantity<>(24.0, LengthUnit.INCHES));
        assertEquals(10.0, result.getValue(), DELTA);
        assertEquals(2.0, result.divide(new Quantity<>(5.0, LengthUnit.FEET)), DELTA);
    }

    @Test
    void testRefactoring_NoBehaviorChange_LargeDataset() {
        for (int i = 1; i <= 100; i++) {
            Quantity<WeightUnit> a = new Quantity<>(i, WeightUnit.KILOGRAM);
            Quantity<WeightUnit> b = new Quantity<>(i * 1000.0, WeightUnit.GRAM);
            assertEquals(2.0 * i, a.add(b).getValue(), DELTA);
            assertEquals(0.0, a.subtract(b).getValue(), DELTA);
            assertEquals(1.0, a.divide(b), DELTA);
        }
    }
}
