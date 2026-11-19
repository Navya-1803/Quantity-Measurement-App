package com.app.quantitymeasurement;

/**
 * LengthUnit is a standalone enum of the supported length units.
 * Feet is the base unit. Each unit knows how to convert a value to and
 * from the base unit, so conversion logic lives in one place.
 *
 * @author Navya Raj
 */
public enum LengthUnit implements IMeasurable {

    FEET(1.0),
    INCHES(1.0 / 12.0),
    // 1 yard = 3 feet
    YARDS(3.0),
    // 1 cm = 0.393701 inches
    CENTIMETERS(0.393701 / 12.0);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    @Override
    public double getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Converts a value in this unit to feet.
     */
    @Override
    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    /**
     * Converts a value in feet to this unit.
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
        for (LengthUnit unit : values()) {
            if (unit.name().equalsIgnoreCase(unitName.trim())) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Invalid unit '" + unitName + "' for LengthUnit");
    }
}
