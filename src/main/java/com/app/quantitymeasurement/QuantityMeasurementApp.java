package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp demonstrates equality, conversion and addition
 * for any measurement category with a single set of generic methods.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    // common null check used by all demonstration methods
    private static void requireQuantity(Quantity<?> quantity) {
        if (quantity == null) {
            throw new IllegalArgumentException("Quantity cannot be null");
        }
    }

    /**
     * Compares two quantities of the same category and logs the result.
     */
    public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> first, Quantity<U> second) {
        requireQuantity(first);
        boolean result = first.equals(second);
        logger.info("Input: {}.equals({}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Converts a quantity to the target unit and logs the result.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {
        requireQuantity(quantity);
        Quantity<U> result = quantity.convertTo(targetUnit);
        logger.info("Input: {}.convertTo({}) -> Output: {}", quantity, targetUnit, result);
        return result;
    }

    /**
     * Adds two quantities, the result is in the unit of the first one.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> first, Quantity<U> second) {
        requireQuantity(first);
        Quantity<U> result = first.add(second);
        logger.info("Input: {}.add({}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Adds two quantities and returns the result in the target unit.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> first, Quantity<U> second, U targetUnit) {
        requireQuantity(first);
        Quantity<U> result = first.add(second, targetUnit);
        logger.info("Input: {}.add({}, {}) -> Output: {}", first, second, targetUnit, result);
        return result;
    }

    public static void main(String[] args) {
        // length operations (UC1 - UC8)
        demonstrateEquality(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES));
        demonstrateEquality(new Quantity<>(1.0, LengthUnit.YARDS), new Quantity<>(36.0, LengthUnit.INCHES));
        demonstrateConversion(new Quantity<>(1.0, LengthUnit.FEET), LengthUnit.INCHES);
        demonstrateAddition(new Quantity<>(1.0, LengthUnit.FEET), new Quantity<>(12.0, LengthUnit.INCHES), LengthUnit.FEET);

        // weight operations (UC9)
        demonstrateEquality(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM));
        demonstrateConversion(new Quantity<>(1.0, WeightUnit.KILOGRAM), WeightUnit.GRAM);
        demonstrateAddition(new Quantity<>(1.0, WeightUnit.KILOGRAM), new Quantity<>(1000.0, WeightUnit.GRAM), WeightUnit.KILOGRAM);

        // cross category comparison is always false
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> oneKilogram = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        logger.info("Input: {}.equals({}) -> Output: {}", oneFoot, oneKilogram, oneFoot.equals(oneKilogram));
    }
}
