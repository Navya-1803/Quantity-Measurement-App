package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp demonstrates equality, conversion, addition,
 * subtraction and division for any measurement category with a single set
 * of generic methods.
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

    /**
     * Subtracts the second quantity from the first, result is in the unit of the first.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> first, Quantity<U> second) {
        requireQuantity(first);
        Quantity<U> result = first.subtract(second);
        logger.info("Input: {}.subtract({}) -> Output: {}", first, second, result);
        return result;
    }

    /**
     * Subtracts the second quantity from the first and returns the result in the target unit.
     */
    public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> first, Quantity<U> second, U targetUnit) {
        requireQuantity(first);
        Quantity<U> result = first.subtract(second, targetUnit);
        logger.info("Input: {}.subtract({}, {}) -> Output: {}", first, second, targetUnit, result);
        return result;
    }

    /**
     * Divides the first quantity by the second and returns the ratio.
     */
    public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> first, Quantity<U> second) {
        requireQuantity(first);
        double result = first.divide(second);
        logger.info("Input: {}.divide({}) -> Output: {}", first, second, result);
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

        // volume operations (UC11)
        Quantity<VolumeUnit> oneLitre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> thousandMillilitre = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> oneGallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        demonstrateEquality(oneLitre, new Quantity<>(1.0, VolumeUnit.LITRE));
        demonstrateEquality(oneLitre, thousandMillilitre);
        demonstrateEquality(oneGallon, new Quantity<>(1.0, VolumeUnit.GALLON));
        demonstrateEquality(oneLitre, new Quantity<>(0.264172, VolumeUnit.GALLON));
        demonstrateEquality(new Quantity<>(500.0, VolumeUnit.MILLILITRE), new Quantity<>(0.5, VolumeUnit.LITRE));
        demonstrateEquality(new Quantity<>(3.78541, VolumeUnit.LITRE), oneGallon);
        demonstrateConversion(oneLitre, VolumeUnit.MILLILITRE);
        demonstrateConversion(new Quantity<>(2.0, VolumeUnit.GALLON), VolumeUnit.LITRE);
        demonstrateConversion(new Quantity<>(500.0, VolumeUnit.MILLILITRE), VolumeUnit.GALLON);
        demonstrateConversion(new Quantity<>(0.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        demonstrateAddition(oneLitre, thousandMillilitre);
        demonstrateAddition(oneLitre, oneGallon, VolumeUnit.MILLILITRE);
        demonstrateAddition(thousandMillilitre, oneGallon, VolumeUnit.GALLON);

        // subtraction (UC12)
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(6.0, LengthUnit.INCHES));
        demonstrateSubtraction(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM));
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(500.0, VolumeUnit.MILLILITRE));
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES);
        demonstrateSubtraction(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5000.0, WeightUnit.GRAM), WeightUnit.GRAM);
        demonstrateSubtraction(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE);
        demonstrateSubtraction(new Quantity<>(5.0, LengthUnit.FEET), new Quantity<>(10.0, LengthUnit.FEET));
        demonstrateSubtraction(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(120.0, LengthUnit.INCHES));

        // division (UC12)
        demonstrateDivision(new Quantity<>(10.0, LengthUnit.FEET), new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(24.0, LengthUnit.INCHES), new Quantity<>(2.0, LengthUnit.FEET));
        demonstrateDivision(new Quantity<>(10.0, WeightUnit.KILOGRAM), new Quantity<>(5.0, WeightUnit.KILOGRAM));
        demonstrateDivision(new Quantity<>(5.0, VolumeUnit.LITRE), new Quantity<>(10.0, VolumeUnit.LITRE));
        demonstrateDivision(new Quantity<>(2000.0, WeightUnit.GRAM), new Quantity<>(1.0, WeightUnit.KILOGRAM));

        // cross category comparison is always false
        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<WeightUnit> oneKilogram = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        logger.info("Input: {}.equals({}) -> Output: {}", oneFoot, oneKilogram, oneFoot.equals(oneKilogram));
    }
}
