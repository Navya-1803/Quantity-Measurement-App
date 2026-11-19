package com.app.quantitymeasurement.unit;

/**
 * WeightUnit is a standalone enum of the supported weight units.
 * Kilogram is the base unit.
 *
 * @author Navya Raj
 */
public enum WeightUnit implements IMeasurable {

    KILOGRAM(1.0),
    // 1 g = 0.001 kg
    GRAM(0.001),
    // 1 lb = 0.453592 kg
    POUND(0.453592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value in this unit to kilograms.
     */
    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a value in kilograms to this unit.
     */
    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    @Override
    public String getUnitName() {
        return name();
    }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        if (unitName == null || unitName.isBlank()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }
        for (WeightUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(unitName.trim())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid unit '" + unitName + "' for WeightUnit");
    }
}
