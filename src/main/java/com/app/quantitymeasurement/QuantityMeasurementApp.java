package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp checks the equality of two measurements given in feet.
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

    public static void main(String[] args) {
        Feet first = new Feet(1.0);
        Feet second = new Feet(1.0);

        boolean result = first.equals(second);
        logger.info("Input: {} and {}", first, second);
        logger.info("Output: {} ({})", result ? "Equal" : "Not Equal", result);
    }
}
