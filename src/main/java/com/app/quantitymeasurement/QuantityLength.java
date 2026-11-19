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

    // common validation for the static conversion method
    private static void validateConversionInput(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
    }

    /**
     * Converts a value from the source unit to the target unit.
     * The value is first converted to feet (base unit) and then to the target unit.
     *
     * @param value  value to convert
     * @param source unit of the given value
     * @param target unit to convert to
     * @return converted value
     * @throws IllegalArgumentException if a unit is null or the value is NaN / infinite
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        validateConversionInput(value, source, target);
        double valueInFeet = value * source.getConversionFactor();
        return valueInFeet / target.getConversionFactor();
    }

    /**
     * Returns a new QuantityLength in the target unit, this object is not modified.
     */
    public QuantityLength convertTo(LengthUnit target) {
        return new QuantityLength(convert(value, unit, target), target);
    }

    /**
     * Adds another length to this one. The result is expressed in the unit of
     * this (first) operand. Both operands stay unchanged.
     *
     * @param other length to add
     * @return new QuantityLength holding the sum
     * @throws IllegalArgumentException if other is null
     */
    public QuantityLength add(QuantityLength other) {
        return add(other, unit);
    }

    /**
     * Adds another length to this one and expresses the result in the given target unit.
     *
     * @param other      length to add
     * @param targetUnit unit of the result
     * @return new QuantityLength holding the sum in the target unit
     * @throws IllegalArgumentException if other or targetUnit is null
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        // add in the base unit and convert the sum to the target unit
        double sumInFeet = this.convertToFeet() + other.convertToFeet();
        return new QuantityLength(sumInFeet / targetUnit.getConversionFactor(), targetUnit);
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
