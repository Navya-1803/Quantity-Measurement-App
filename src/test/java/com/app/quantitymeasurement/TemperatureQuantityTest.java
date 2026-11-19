package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureQuantityTest {

    private static final double DELTA = 1e-6;

    // ---------- equality ----------

    @Test
    void testTemperatureEquality_CelsiusToCelsius_SameValue() {
        assertEquals(new Quantity<>(25.0, TemperatureUnit.CELSIUS), new Quantity<>(25.0, TemperatureUnit.CELSIUS));
    }

    @Test
    void testTemperatureEquality_FahrenheitToFahrenheit_SameValue() {
        assertEquals(new Quantity<>(98.6, TemperatureUnit.FAHRENHEIT), new Quantity<>(98.6, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_0Celsius32Fahrenheit() {
        assertEquals(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_100Celsius212Fahrenheit() {
        assertEquals(new Quantity<>(100.0, TemperatureUnit.CELSIUS), new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    void testTemperatureEquality_CelsiusToFahrenheit_Negative40Equal() {
        assertEquals(new Quantity<>(-40.0, TemperatureUnit.CELSIUS), new Quantity<>(-40.0, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    void testTemperatureEquality_SymmetricProperty() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(100.0, TemperatureUnit.CELSIUS);
        Quantity<TemperatureUnit> kelvin = new Quantity<>(373.15, TemperatureUnit.KELVIN);
        assertTrue(celsius.equals(kelvin) && kelvin.equals(celsius));
    }

    @Test
    void testTemperatureEquality_ReflexiveProperty() {
        Quantity<TemperatureUnit> celsius = new Quantity<>(10.0, TemperatureUnit.CELSIUS);
        assertEquals(celsius, celsius);
    }

    @Test
    void testTemperatureDifferentValuesInequality() {
        assertNotEquals(new Quantity<>(10.0, TemperatureUnit.CELSIUS), new Quantity<>(10.0, TemperatureUnit.FAHRENHEIT));
    }

    // ---------- conversion ----------

    @Test
    void testTemperatureConversion_CelsiusToFahrenheit_VariousValues() {
        assertEquals(212.0, new Quantity<>(100.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), DELTA);
        assertEquals(122.0, new Quantity<>(50.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), DELTA);
        assertEquals(-4.0, new Quantity<>(-20.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversion_FahrenheitToCelsius_VariousValues() {
        assertEquals(0.0, new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS).getValue(), DELTA);
        assertEquals(37.0, new Quantity<>(98.6, TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversion_RoundTrip_PreservesValue() {
        Quantity<TemperatureUnit> original = new Quantity<>(37.5, TemperatureUnit.CELSIUS);
        assertEquals(original, original.convertTo(TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.KELVIN).convertTo(TemperatureUnit.CELSIUS));
    }

    @Test
    void testTemperatureConversion_SameUnit() {
        assertEquals(42.0, new Quantity<>(42.0, TemperatureUnit.KELVIN).convertTo(TemperatureUnit.KELVIN).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversion_ZeroValue() {
        assertEquals(273.15, new Quantity<>(0.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.KELVIN).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversion_NegativeValues() {
        assertEquals(-40.0, new Quantity<>(-40.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversion_LargeValues() {
        assertEquals(1832.0, new Quantity<>(1000.0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT).getValue(), DELTA);
    }

    @Test
    void testTemperatureConversionPrecision_Epsilon() {
        assertEquals(new Quantity<>(37.0, TemperatureUnit.CELSIUS), new Quantity<>(98.6, TemperatureUnit.FAHRENHEIT));
    }

    @Test
    void testTemperatureConversionEdgeCase_VerySmallDifference() {
        assertNotEquals(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(0.001, TemperatureUnit.CELSIUS));
    }

    // ---------- unsupported operations ----------

    @Test
    void testTemperatureUnsupportedOperation_Add() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS).add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureUnsupportedOperation_Subtract() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS).subtract(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureUnsupportedOperation_Divide() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS).divide(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
    }

    @Test
    void testTemperatureUnsupportedOperation_ErrorMessage() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS).add(new Quantity<>(50.0, TemperatureUnit.CELSIUS)));
        assertTrue(exception.getMessage().startsWith("Temperature does not support"));
    }

    @Test
    void testTemperatureCrossUnitAdditionAttempt() {
        assertThrows(UnsupportedOperationException.class,
                () -> new Quantity<>(100.0, TemperatureUnit.CELSIUS).add(new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT), TemperatureUnit.KELVIN));
    }

    // ---------- category isolation ----------

    @Test
    void testTemperatureVsLengthIncompatibility() {
        assertNotEquals(new Quantity<>(100.0, TemperatureUnit.CELSIUS), new Quantity<>(100.0, LengthUnit.FEET));
    }

    @Test
    void testTemperatureVsWeightIncompatibility() {
        assertNotEquals(new Quantity<>(50.0, TemperatureUnit.CELSIUS), new Quantity<>(50.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testTemperatureVsVolumeIncompatibility() {
        assertNotEquals(new Quantity<>(1.0, TemperatureUnit.CELSIUS), new Quantity<>(1.0, VolumeUnit.LITRE));
    }

    // ---------- validation and integration ----------

    @Test
    void testTemperatureNullUnitValidation() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity<TemperatureUnit>(10.0, null));
    }

    @Test
    void testTemperatureNullOperandValidation_InComparison() {
        assertNotEquals(null, new Quantity<>(10.0, TemperatureUnit.CELSIUS));
    }

    @Test
    void testOperationSupportMethods_LengthUnitAddition() {
        assertTrue(LengthUnit.FEET.supportsArithmetic());
        assertDoesNotThrow(() -> LengthUnit.FEET.validateOperationSupport("ADD"));
    }

    @Test
    void testOperationSupportMethods_WeightUnitDivision() {
        assertTrue(WeightUnit.GRAM.supportsArithmetic());
        assertDoesNotThrow(() -> WeightUnit.GRAM.validateOperationSupport("DIVIDE"));
    }

    @Test
    void testIMeasurableInterface_Evolution_BackwardCompatible() {
        // existing units still work after the new default methods were added
        assertEquals(new Quantity<>(2.0, LengthUnit.FEET), new Quantity<>(1.0, LengthUnit.FEET).add(new Quantity<>(12.0, LengthUnit.INCHES)));
        assertTrue(VolumeUnit.LITRE.supportsArithmetic());
    }

    @Test
    void testTemperatureDefaultMethodInheritance() {
        // length, weight and volume use the default implementation from IMeasurable
        assertEquals(IMeasurable.supportsArithmetic.isSupported(), VolumeUnit.GALLON.supportsArithmetic());
    }

    @Test
    void testTemperatureIntegrationWithGenericQuantity() {
        assertTrue(QuantityMeasurementApp.demonstrateEquality(new Quantity<>(0.0, TemperatureUnit.CELSIUS), new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT)));
        assertEquals(new Quantity<>(212.0, TemperatureUnit.FAHRENHEIT),
                QuantityMeasurementApp.demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT));
    }
}
