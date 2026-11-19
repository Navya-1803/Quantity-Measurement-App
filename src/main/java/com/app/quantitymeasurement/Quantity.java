package com.app.quantitymeasurement;

/**
 * Quantity is a generic measurement made of a value and a unit. It replaces
 * QuantityLength and QuantityWeight, any unit enum implementing
 * {@link IMeasurable} can be used without writing a new class.
 *
 * @param <U> unit type, for example LengthUnit or WeightUnit
 * @author Navya Raj
 */
public class Quantity<U extends IMeasurable> {

    // tolerance used while comparing floating point values
    private static final double EPSILON = 1e-6;

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
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

    public U getUnit() {
        return unit;
    }

    // value of this quantity in the base unit of its category
    private double toBaseUnit() {
        return unit.convertToBaseUnit(value);
    }

    /**
     * Returns a new Quantity in the target unit, this object is not modified.
     *
     * @throws IllegalArgumentException if target unit is null
     */
    public Quantity<U> convertTo(U targetUnit) {
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        return new Quantity<>(targetUnit.convertFromBaseUnit(toBaseUnit()), targetUnit);
    }

    /**
     * Adds another quantity, the result is in the unit of this (first) operand.
     */
    public Quantity<U> add(Quantity<U> other) {
        return add(other, unit);
    }

    /**
     * Adds another quantity and expresses the result in the target unit.
     *
     * @throws IllegalArgumentException if other or target unit is null
     */
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to add cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        double sum = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity<>(targetUnit.convertFromBaseUnit(sum), targetUnit);
    }

    /**
     * Subtracts another quantity from this one, the result is in the unit of
     * this (first) operand and rounded to two decimal places.
     * Subtraction is not commutative: a - b is not the same as b - a.
     *
     * @throws IllegalArgumentException if other is null or of a different category
     */
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, unit);
    }

    /**
     * Subtracts another quantity from this one and expresses the result in the
     * target unit, rounded to two decimal places.
     *
     * @throws IllegalArgumentException if other / target unit is null or categories differ
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to subtract cannot be null");
        }
        if (targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cannot subtract quantities of different categories");
        }
        double difference = this.toBaseUnit() - other.toBaseUnit();
        return new Quantity<>(roundToTwoDecimals(targetUnit.convertFromBaseUnit(difference)), targetUnit);
    }

    /**
     * Divides this quantity by another one of the same category.
     * The result is a plain number (ratio), e.g. 24 INCHES / 2 FEET = 1.0
     *
     * @return ratio of this quantity to the other quantity
     * @throws IllegalArgumentException if other is null or of a different category
     * @throws ArithmeticException      if other is zero
     */
    public double divide(Quantity<U> other) {
        if (other == null) {
            throw new IllegalArgumentException("Quantity to divide by cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cannot divide quantities of different categories");
        }
        double divisor = other.toBaseUnit();
        if (Math.abs(divisor) < EPSILON) {
            throw new ArithmeticException("Cannot divide by zero quantity");
        }
        return this.toBaseUnit() / divisor;
    }

    // rounds a value to two decimal places
    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Quantity<?> other)) {
            return false;
        }
        // quantities of different categories (e.g. FEET vs KILOGRAM) are never equal
        if (this.unit.getClass() != other.unit.getClass()) {
            return false;
        }
        return Math.abs(this.toBaseUnit() - other.toBaseUnit()) < EPSILON;
    }

    @Override
    public int hashCode() {
        // unit category is part of the hash so that different categories spread out
        return 31 * unit.getClass().hashCode() + Long.hashCode(Math.round(toBaseUnit() / EPSILON));
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}
