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
