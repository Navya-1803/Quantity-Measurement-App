package com.app.quantitymeasurement;

/**
 * Quantity is a generic measurement made of a value and a unit. It replaces
 * QuantityLength and QuantityWeight, any unit enum implementing
 * {@link IMeasurable} can be used without writing a new class. Units that
 * do not support arithmetic (like temperature) can still be compared and
 * converted.
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
     * Adds another quantity and expresses the result in the target unit,
     * rounded to two decimal places.
     *
     * @throws IllegalArgumentException if other / target unit is null or categories differ
     */
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
        return toTargetUnit(baseResult, targetUnit);
    }

    /**
     * Subtracts another quantity from this one, the result is in the unit of
     * this (first) operand. Subtraction is not commutative.
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
        validateArithmeticOperands(other, targetUnit, true);
        double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        return toTargetUnit(baseResult, targetUnit);
    }

    /**
     * Divides this quantity by another one of the same category. The result
     * is a plain ratio and is not rounded, e.g. 24 INCHES / 2 FEET = 1.0
     *
     * @throws IllegalArgumentException if other is null or of a different category
     * @throws ArithmeticException      if other is zero
     */
    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    /**
     * Single place for all operand validation so every operation behaves
     * and reports errors the same way.
     */
    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {
        if (other == null) {
            throw new IllegalArgumentException("Operand quantity cannot be null");
        }
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cannot perform arithmetic between different measurement categories: "
                    + this.unit.getClass().getSimpleName() + " and " + other.unit.getClass().getSimpleName());
        }
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
            throw new IllegalArgumentException("Operand values must be finite numbers");
        }
        if (targetUnitRequired && targetUnit == null) {
            throw new IllegalArgumentException("Target unit cannot be null");
        }
    }

    /**
     * Checks that the unit supports the operation, then converts both
     * operands to the base unit and applies the operation.
     *
     * @throws UnsupportedOperationException if the unit category does not allow the operation
     */
    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        unit.validateOperationSupport(operation.name());
        return operation.compute(this.toBaseUnit(), other.toBaseUnit());
    }

    // converts a base unit result to the target unit, rounded to two decimals
    private Quantity<U> toTargetUnit(double baseValue, U targetUnit) {
        return new Quantity<>(roundToTwoDecimals(targetUnit.convertFromBaseUnit(baseValue)), targetUnit);
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
