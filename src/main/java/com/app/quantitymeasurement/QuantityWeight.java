package com.app.quantitymeasurement;

/**
 * QuantityWeight represents a weight measurement with a value and a unit.
 * All conversion work is delegated to {@link WeightUnit}, this class only
 * takes care of equality and arithmetic. Weight and length are different
 * categories and can never be compared with each other.
 *
 * @author Navya Raj
 */
public class QuantityWeight {

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    private final double value;
    private final WeightUnit unit;

    public QuantityWeight(double value, WeightUnit unit) {
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

    public WeightUnit getUnit() {
        return unit;
    }

    // value of this weight in kilograms
    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    /**
     * Converts a value from the source unit to the target unit.
     *
     * @throws IllegalArgumentException if a unit is null or the value is NaN / infinite
     */
    public static double convert(double value, WeightUnit source, WeightUnit target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target unit cannot be null");
        }
        validateValue(value);
        return target.convertFromBaseUnit(source.convertToBaseUnit(value));
    }

    /**
     * Returns a new QuantityWeight in the target unit, this object is not modified.
     */
    public QuantityWeight convertTo(WeightUnit target) {
        if (target == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return new QuantityWeight(target.convertFromBaseUnit(toBaseUnit()), target);
    }

    /**
     * Adds another weight, the result is in the unit of this (first) operand.
     */
    public QuantityWeight add(QuantityWeight other) {
        return add(other, unit);
    }

    /**
     * Adds another weight and expresses the result in the given target unit.
     *
     * @throws IllegalArgumentException if other or targetUnit is null
     */
    public QuantityWeight add(QuantityWeight other, WeightUnit targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Weight to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sum = this.toBaseUnit() + other.toBaseUnit();
        return new QuantityWeight(targetUnit.convertFromBaseUnit(sum), targetUnit);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        // a weight is never equal to a length or any other category
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QuantityWeight other = (QuantityWeight) obj;
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
