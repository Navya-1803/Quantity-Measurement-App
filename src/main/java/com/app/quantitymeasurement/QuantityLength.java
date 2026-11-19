package com.app.quantitymeasurement;

/**
 * QuantityLength represents a length measurement with a value and a unit.
 * All conversion work is delegated to {@link LengthUnit}, this class only
 * takes care of equality and arithmetic.
 *
 * @author Navya Raj
 */
public class QuantityLength {

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    private final double value;
    private final LengthUnit unit;

    public QuantityLength(double value, LengthUnit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        validateValue(value);
        this.value = value;
        this.unit = unit;
    }

    // only real numbers are valid measurements
    private static void validateValue(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
    }

    public double getValue() {
        return value;
    }

    public LengthUnit getUnit() {
        return unit;
    }

    // value of this length in feet
    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    /**
     * Converts a value from the source unit to the target unit.
     *
     * @throws IllegalArgumentException if a unit is null or the value is NaN / infinite
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target unit cannot be null");
        }
        validateValue(value);
        return target.convertFromBaseUnit(source.convertToBaseUnit(value));
    }

    /**
     * Returns a new QuantityLength in the target unit, this object is not modified.
     */
    public QuantityLength convertTo(LengthUnit target) {
        if (target == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return new QuantityLength(target.convertFromBaseUnit(toBaseUnit()), target);
    }

    /**
     * Adds another length, the result is in the unit of this (first) operand.
     */
    public QuantityLength add(QuantityLength other) {
        return add(other, unit);
    }

    /**
     * Adds another length and expresses the result in the given target unit.
     *
     * @throws IllegalArgumentException if other or targetUnit is null
     */
    public QuantityLength add(QuantityLength other, LengthUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Length to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sum = this.toBaseUnit() + other.toBaseUnit();
        return new QuantityLength(targetUnit.convertFromBaseUnit(sum), targetUnit);
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
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        // rounding keeps hashCode consistent with the epsilon based equals
        return Long.hashCode(Math.round(toBaseUnit() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit + ")";
    }
}
