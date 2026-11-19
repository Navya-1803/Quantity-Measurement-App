package com.app.quantitymeasurement;

/**
 * WeightUnit is a standalone enum of the supported weight units.
 * Kilogram is the base unit.
 *
 * @author Navya Raj
 */
public enum WeightUnit {

    KILOGRAM(1.0),
    // 1 g = 0.001 kg
    GRAM(0.001),
    // 1 lb = 0.453592 kg
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value in this unit to kilograms.
     */
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a value in kilograms to this unit.
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }
}
