package com.app.quantitymeasurement.unit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TemperatureUnitTest {

    private static final double DELTA = 1e-6;

    @Test
    void testTemperatureUnit_AllConstants() {
        assertEquals(3, TemperatureUnit.values().length);
        assertNotNull(TemperatureUnit.valueOf("CELSIUS"));
        assertNotNull(TemperatureUnit.valueOf("FAHRENHEIT"));
        assertNotNull(TemperatureUnit.valueOf("KELVIN"));
    }

    @Test
    void testTemperatureUnit_NameMethod() {
        assertEquals("FAHRENHEIT", TemperatureUnit.FAHRENHEIT.getUnitName());
    }

    @Test
    void testTemperatureUnit_ConversionFactor() {
        assertEquals(1.0, TemperatureUnit.KELVIN.getConversionFactor(), DELTA);
    }

    @Test
    void testTemperatureUnit_NonLinearConversion() {
        // 0 F is not 0 C, a plain factor could never give this result
        assertEquals(-17.777778, TemperatureUnit.FAHRENHEIT.convertToBaseUnit(0.0), DELTA);
        assertEquals(32.0, TemperatureUnit.FAHRENHEIT.convertFromBaseUnit(0.0), DELTA);
    }

    @Test
    void testConvertToBaseUnit_KelvinToCelsius() {
        assertEquals(0.0, TemperatureUnit.KELVIN.convertToBaseUnit(273.15), DELTA);
    }

    @Test
    void testConvertFromBaseUnit_CelsiusToKelvin() {
        assertEquals(373.15, TemperatureUnit.KELVIN.convertFromBaseUnit(100.0), DELTA);
    }

    @Test
    void testTemperatureEnumImplementsIMeasurable() {
        assertInstanceOf(IMeasurable.class, TemperatureUnit.CELSIUS);
    }

    @Test
    void testOperationSupportMethods_TemperatureUnitAddition() {
        assertFalse(TemperatureUnit.CELSIUS.supportsArithmetic());
        assertThrows(UnsupportedOperationException.class, () -> TemperatureUnit.CELSIUS.validateOperationSupport("ADD"));
    }

    @Test
    void testOperationSupportMethods_TemperatureUnitDivision() {
        assertThrows(UnsupportedOperationException.class, () -> TemperatureUnit.KELVIN.validateOperationSupport("DIVIDE"));
    }

    @Test
    void testTemperatureValidateOperationSupport_MethodBehavior() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class,
                () -> TemperatureUnit.FAHRENHEIT.validateOperationSupport("SUBTRACT"));
        assertTrue(exception.getMessage().contains("SUBTRACT"));
    }

    @Test
    void testGetUnitInstance_ValidName() {
        assertEquals(TemperatureUnit.KELVIN, TemperatureUnit.KELVIN.getUnitInstance("kelvin"));
    }

    @Test
    void testGetUnitInstance_InvalidName() {
        assertThrows(IllegalArgumentException.class, () -> TemperatureUnit.KELVIN.getUnitInstance("UNKNOWN"));
    }

    @Test
    void testGetMeasurementType() {
        assertEquals("TemperatureUnit", TemperatureUnit.KELVIN.getMeasurementType());
    }
}
