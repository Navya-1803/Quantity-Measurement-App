package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp demonstrates equality, conversion and addition
 * of lengths (QuantityLength) and weights (QuantityWeight).
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

    /**
     * Adds two lengths and logs the result, the result is in the unit of the first length.
     */
    public static QuantityLength demonstrateLengthAddition(QuantityLength first, QuantityLength second) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to add cannot be null");
        }
        QuantityLength result = first.add(second);
        logger.info("Input: add({}, {}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Overloaded version which adds two lengths and returns the result in the target unit.
     */
    public static QuantityLength demonstrateLengthAddition(QuantityLength first, QuantityLength second, LengthUnit targetUnit) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to add cannot be null");
        }
        QuantityLength result = first.add(second, targetUnit);
        logger.info("Input: add({}, {}, {}) -> Output: {}", first, second, targetUnit, result);
        return result;
    }

    /**
     * Compares two weights and logs the result.
     */
    public static boolean demonstrateWeightEquality(QuantityWeight first, QuantityWeight second) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to compare cannot be null");
        }
        boolean result = first.equals(second);
        logger.info("Input: {}.equals({}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Converts a weight to the target unit and logs the result.
     */
    public static QuantityWeight demonstrateWeightConversion(QuantityWeight weight, WeightUnit target) {
        if (weight == null) {
            throw new IllegalArgumentException("Quantity to convert cannot be null");
        }
        QuantityWeight result = weight.convertTo(target);
        logger.info("Input: {}.convertTo({}) -> Output: {}", weight, target, result);
        return result;
    }

    /**
     * Adds two weights, result is in the unit of the first weight.
     */
    public static QuantityWeight demonstrateWeightAddition(QuantityWeight first, QuantityWeight second) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to add cannot be null");
        }
        QuantityWeight result = first.add(second);
        logger.info("Input: {}.add({}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Adds two weights and returns the result in the target unit.
     */
    public static QuantityWeight demonstrateWeightAddition(QuantityWeight first, QuantityWeight second, WeightUnit targetUnit) {
        if (first == null) {
            throw new IllegalArgumentException("Quantity to add cannot be null");
        }
        QuantityWeight result = first.add(second, targetUnit);
        logger.info("Input: {}.add({}, {}) -> Output: {}", first, second, targetUnit, result);
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

        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(2.0, LengthUnit.FEET));
        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES));
        demonstrateLengthAddition(new QuantityLength(12.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.FEET));
        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET));
        demonstrateLengthAddition(new QuantityLength(36.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.YARDS));
        demonstrateLengthAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS), new QuantityLength(1.0, LengthUnit.INCHES));
        demonstrateLengthAddition(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(0.0, LengthUnit.INCHES));
        demonstrateLengthAddition(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(-2.0, LengthUnit.FEET));

        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.FEET);
        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.INCHES);
        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES), LengthUnit.YARDS);
        demonstrateLengthAddition(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET), LengthUnit.YARDS);
        demonstrateLengthAddition(new QuantityLength(36.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.YARDS), LengthUnit.FEET);
        demonstrateLengthAddition(new QuantityLength(2.54, LengthUnit.CENTIMETERS), new QuantityLength(1.0, LengthUnit.INCHES), LengthUnit.CENTIMETERS);
        demonstrateLengthAddition(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(0.0, LengthUnit.INCHES), LengthUnit.YARDS);
        demonstrateLengthAddition(new QuantityLength(5.0, LengthUnit.FEET), new QuantityLength(-2.0, LengthUnit.FEET), LengthUnit.INCHES);

        demonstrateLengthConversion(new QuantityLength(2.54, LengthUnit.CENTIMETERS), LengthUnit.INCHES);
        logger.info("Input: LengthUnit.FEET.convertToBaseUnit(12.0) -> Output: {}", LengthUnit.FEET.convertToBaseUnit(12.0));
        logger.info("Input: LengthUnit.INCHES.convertToBaseUnit(12.0) -> Output: {}", LengthUnit.INCHES.convertToBaseUnit(12.0));
    
        // weight equality
        demonstrateWeightEquality(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1.0, WeightUnit.KILOGRAM));
        demonstrateWeightEquality(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM));
        demonstrateWeightEquality(new QuantityWeight(2.0, WeightUnit.POUND), new QuantityWeight(2.0, WeightUnit.POUND));
        demonstrateWeightEquality(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(2.20462, WeightUnit.POUND));
        demonstrateWeightEquality(new QuantityWeight(500.0, WeightUnit.GRAM), new QuantityWeight(0.5, WeightUnit.KILOGRAM));
        demonstrateWeightEquality(new QuantityWeight(1.0, WeightUnit.POUND), new QuantityWeight(453.592, WeightUnit.GRAM));

        // weight conversion
        demonstrateWeightConversion(new QuantityWeight(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        demonstrateWeightConversion(new QuantityWeight(2.0, WeightUnit.POUND), WeightUnit.KILOGRAM);
        demonstrateWeightConversion(new QuantityWeight(500.0, WeightUnit.GRAM), WeightUnit.POUND);
        demonstrateWeightConversion(new QuantityWeight(0.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);

        // weight addition
        demonstrateWeightAddition(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(2.0, WeightUnit.KILOGRAM));
        demonstrateWeightAddition(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM));
        demonstrateWeightAddition(new QuantityWeight(500.0, WeightUnit.GRAM), new QuantityWeight(0.5, WeightUnit.KILOGRAM));
        demonstrateWeightAddition(new QuantityWeight(1.0, WeightUnit.KILOGRAM), new QuantityWeight(1000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        demonstrateWeightAddition(new QuantityWeight(1.0, WeightUnit.POUND), new QuantityWeight(453.592, WeightUnit.GRAM), WeightUnit.POUND);
        demonstrateWeightAddition(new QuantityWeight(2.0, WeightUnit.KILOGRAM), new QuantityWeight(4.0, WeightUnit.POUND), WeightUnit.KILOGRAM);

        // weight and length are different categories
        logger.info("Input: {}.equals({}) -> Output: {}", new QuantityWeight(1.0, WeightUnit.KILOGRAM),
                new QuantityLength(1.0, LengthUnit.FEET),
                new QuantityWeight(1.0, WeightUnit.KILOGRAM).equals(new QuantityLength(1.0, LengthUnit.FEET)));
    }
}
