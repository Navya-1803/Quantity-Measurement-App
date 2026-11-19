package com.app.quantitymeasurement.unit;

import java.util.function.DoubleBinaryOperator;

/**
 * ArithmeticOperation lists the arithmetic operations supported on
 * quantities. Each constant carries its own computation as a lambda, so a
 * new operation can be added without touching the Quantity class
 * (Open/Closed Principle).
 *
 * @author Navya Raj
 */
public enum ArithmeticOperation {

    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> {
        // dividing by zero has no meaning for quantities
        if (b == 0.0) {
            throw new ArithmeticException("Cannot divide by zero quantity");
        }
        return a / b;
    });

    private final DoubleBinaryOperator operator;

    ArithmeticOperation(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    /**
     * Applies this operation on two values that are already in the base unit.
     */
    public double compute(double firstBaseValue, double secondBaseValue) {
        return operator.applyAsDouble(firstBaseValue, secondBaseValue);
    }
}
