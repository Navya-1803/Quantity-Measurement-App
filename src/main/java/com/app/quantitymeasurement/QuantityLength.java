package com.app.quantitymeasurement;

/**
 * QuantityLength represents a length measurement with a value and a unit.
 * It replaces the separate Feet and Inches classes (DRY principle), every
 * unit is handled by the same equality logic.
 *
 * @author Navya Raj
 */
public class QuantityLength {

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    /**
     * Supported length units with their conversion factor to the base unit (feet).
     */
    public enum LengthUnit {
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

        public double getConversionFactor() {
            return conversionFactor;
        }
    }

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    // converts the value into feet so that different units can be compared
    private double convertToFeet() {
        return value * unit.getConversionFactor();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QuantityLength other = (QuantityLength) obj;
        return Math.abs(this.convertToFeet() - other.convertToFeet()) < EPSILON;
    }

    @Override
    public int hashCode() {
        // rounding keeps hashCode consistent with the epsilon based equals
        return Long.hashCode(Math.round(convertToFeet() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}
