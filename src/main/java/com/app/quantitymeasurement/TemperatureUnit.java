package com.app.quantitymeasurement;

import java.util.function.DoubleUnaryOperator;

/**
 * TemperatureUnit is the enum of supported temperature units. Celsius is the
 * base unit. Temperature conversion is not a simple multiplication (it needs
 * an offset), so every constant carries its own conversion lambdas.
 * Arithmetic like adding two temperatures is not meaningful and is rejected.
 *
 * @author Navya Raj
 */
public enum TemperatureUnit implements IMeasurable {

    CELSIUS(celsius -> celsius, celsius -> celsius),
    // F = C * 9/5 + 32
    FAHRENHEIT(fahrenheit -> (fahrenheit - 32) * 5 / 9, celsius -> celsius * 9 / 5 + 32),
    // K = C + 273.15
    KELVIN(kelvin -> kelvin - 273.15, celsius -> celsius + 273.15);

    // temperature does not support arithmetic operations
    private static final SupportsArithmetic TEMPERATURE_ARITHMETIC = () -> false;

    private final DoubleUnaryOperator toCelsius;
    private final DoubleUnaryOperator fromCelsius;

    TemperatureUnit(DoubleUnaryOperator toCelsius, DoubleUnaryOperator fromCelsius) {
        this.toCelsius = toCelsius;
        this.fromCelsius = fromCelsius;
    }

    /**
     * Temperature has no single conversion factor, 1.0 is returned only to
     * satisfy the interface. Conversion is done by the methods below.
     */
    @Override
    public double getConversionFactor() {
        return 1.0;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toCelsius.applyAsDouble(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromCelsius.applyAsDouble(baseValue);
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public boolean supportsArithmetic() {
        return TEMPERATURE_ARITHMETIC.isSupported();
    }

    @Override
    public void validateOperationSupport(String operation) {
        if (!supportsArithmetic()) {
            throw new UnsupportedOperationException("Temperature does not support " + operation
                    + " operation, only comparison and conversion are allowed");
        }
    }
}
