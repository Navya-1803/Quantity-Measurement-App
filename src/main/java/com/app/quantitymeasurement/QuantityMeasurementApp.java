package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp checks the equality of two measurements given in
 * feet or in inches. Feet and inches are still treated separately.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    /**
     * Feet represents a measurement in feet. It is immutable, the value can
     * only be set once through the constructor.
     */
    public static class Feet {

        private final double value;

        public Feet(double value) {
            // reject NaN and infinite values, only real numbers are valid measurements
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            // same reference is always equal
            if (this == obj) {
                return true;
            }
            // null or a different type can never be equal
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Feet other = (Feet) obj;
            // Double.compare avoids precision problems of ==
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }

        @Override
        public String toString() {
            return value + " ft";
        }
    }

    /**
     * Inches represents a measurement in inches, built the same way as Feet.
     */
    public static class Inches {

        private final double value;

        public Inches(double value) {
            // reject NaN and infinite values, only real numbers are valid measurements
            if (!Double.isFinite(value)) {
                throw new IllegalArgumentException("Value must be a finite number");
            }
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Inches other = (Inches) obj;
            return Double.compare(this.value, other.value) == 0;
        }

        @Override
        public int hashCode() {
            return Double.hashCode(value);
        }

        @Override
        public String toString() {
            return value + " inch";
        }
    }

    /**
     * Creates two Feet objects from the given values and checks their equality.
     */
    public static boolean demonstrateFeetEquality(double first, double second) {
        Feet feet1 = new Feet(first);
        Feet feet2 = new Feet(second);
        boolean result = feet1.equals(feet2);
        logger.info("Input: {} and {} -> Output: {} ({})", feet1, feet2, result ? "Equal" : "Not Equal", result);
        return result;
    }

    /**
     * Creates two Inches objects from the given values and checks their equality.
     */
    public static boolean demonstrateInchesEquality(double first, double second) {
        Inches inches1 = new Inches(first);
        Inches inches2 = new Inches(second);
        boolean result = inches1.equals(inches2);
        logger.info("Input: {} and {} -> Output: {} ({})", inches1, inches2, result ? "Equal" : "Not Equal", result);
        return result;
    }

    public static void main(String[] args) {
        demonstrateInchesEquality(1.0, 1.0);
        demonstrateFeetEquality(1.0, 1.0);
    }
}
