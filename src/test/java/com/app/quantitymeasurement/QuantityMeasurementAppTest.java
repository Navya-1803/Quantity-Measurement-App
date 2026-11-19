package com.app.quantitymeasurement;

import com.app.quantitymeasurement.QuantityMeasurementApp.Feet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testEquality_SameValue() {
        assertTrue(new Feet(1.0).equals(new Feet(1.0)), "1.0 ft should be equal to 1.0 ft");
    }

    @Test
    void testEquality_DifferentValue() {
        assertFalse(new Feet(1.0).equals(new Feet(2.0)), "1.0 ft should not be equal to 2.0 ft");
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new Feet(1.0).equals(null), "a value should not be equal to null");
    }

    @Test
    void testEquality_NonNumericInput() {
        assertFalse(new Feet(1.0).equals("1.0"), "a value should not be equal to a non numeric input");
    }

    @Test
    void testEquality_SameReference() {
        Feet feet = new Feet(1.0);
        assertTrue(feet.equals(feet), "a value should be equal to itself");
    }

    @Test
    void testEquality_Symmetric() {
        Feet first = new Feet(3.5);
        Feet second = new Feet(3.5);
        assertTrue(first.equals(second) && second.equals(first), "equality should be symmetric");
    }

    @Test
    void testEquality_Transitive() {
        Feet a = new Feet(2.0);
        Feet b = new Feet(2.0);
        Feet c = new Feet(2.0);
        assertTrue(a.equals(b) && b.equals(c) && a.equals(c), "equality should be transitive");
    }

    @Test
    void testEquality_Consistent() {
        Feet first = new Feet(4.0);
        Feet second = new Feet(4.0);
        for (int i = 0; i < 5; i++) {
            assertTrue(first.equals(second), "repeated calls should return the same result");
        }
    }

    @Test
    void testHashCode_EqualObjects() {
        assertEquals(new Feet(1.0).hashCode(), new Feet(1.0).hashCode(), "equal objects must have equal hash codes");
    }

    @Test
    void testConstructor_InvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> new Feet(Double.NaN), "NaN should be rejected");
        assertThrows(IllegalArgumentException.class, () -> new Feet(Double.POSITIVE_INFINITY), "infinity should be rejected");
    }

    @Test
    void testMain_RunsWithoutException() {
        assertDoesNotThrow(() -> QuantityMeasurementApp.main(new String[]{}));
    }
}
