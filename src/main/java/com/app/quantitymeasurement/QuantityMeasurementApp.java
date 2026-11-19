package com.app.quantitymeasurement;

import com.app.quantitymeasurement.QuantityLength.LengthUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp demonstrates equality checks between lengths of
 * any supported unit using the QuantityLength class.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    /**
     * Compares two lengths and logs the result.
     */
    public static boolean demonstrateLengthEquality(QuantityLength first, QuantityLength second) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to compare cannot be null");
        }
        boolean result = first.equals(second);
        logger.info("Input: {} and {} -> Output: {} ({})", first, second, result ? "Equal" : "Not Equal", result);
        return result;
    }

    /**
     * Converts a value from one unit to another and logs the result.
     */
    public static double demonstrateLengthConversion(double value, LengthUnit source, LengthUnit target) {
        double result = QuantityLength.convert(value, source, target);
        logger.info("Input: convert({}, {}, {}) -> Output: {}", value, source, target, result);
        return result;
    }

    /**
     * Overloaded version which converts an existing QuantityLength.
     */
    public static QuantityLength demonstrateLengthConversion(QuantityLength length, LengthUnit target) {
        if (length == null) {
            throw new IllegalArgumentException("Quantity to convert cannot be null");
        }
        QuantityLength result = length.convertTo(target);
        logger.info("Input: {}.convertTo({}) -> Output: {}", length, target, result);
        return result;
    }

    public static void main(String[] args) {
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(36.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(2.0, LengthUnit.YARDS), new QuantityLength(2.0, LengthUnit.YARDS));
        demonstrateLengthEquality(new QuantityLength(2.0, LengthUnit.CENTIMETERS), new QuantityLength(2.0, LengthUnit.CENTIMETERS));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(0.393701, LengthUnit.INCHES));

        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES);
        demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
        demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS);
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES);
        demonstrateLengthConversion(0.0, LengthUnit.FEET, LengthUnit.INCHES);
    }
}
