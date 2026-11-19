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

    public static void main(String[] args) {
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.FEET), new QuantityLength(12.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.INCHES), new QuantityLength(1.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(3.0, LengthUnit.FEET));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.YARDS), new QuantityLength(36.0, LengthUnit.INCHES));
        demonstrateLengthEquality(new QuantityLength(2.0, LengthUnit.YARDS), new QuantityLength(2.0, LengthUnit.YARDS));
        demonstrateLengthEquality(new QuantityLength(2.0, LengthUnit.CENTIMETERS), new QuantityLength(2.0, LengthUnit.CENTIMETERS));
        demonstrateLengthEquality(new QuantityLength(1.0, LengthUnit.CENTIMETERS), new QuantityLength(0.393701, LengthUnit.INCHES));
    }
}
