package com.app.quantitymeasurement;

/**
 * IMeasurable is the common contract for every unit category (length,
 * weight, ...). Any enum implementing it can be used with {@link Quantity}.
 *
 * @author Navya Raj
 */
public interface IMeasurable {

    /**
     * @return factor to convert a value of this unit to the base unit of its category
     */
    double getConversionFactor();

    /**
     * Converts a value of this unit to the base unit.
     */
    double convertToBaseUnit(double value);

    /**
     * Converts a value from the base unit to this unit.
     */
    double convertFromBaseUnit(double baseValue);

    /**
     * @return readable name of the unit, e.g. "FEET"
     */
    String getUnitName();
}
