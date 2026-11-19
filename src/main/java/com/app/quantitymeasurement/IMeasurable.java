package com.app.quantitymeasurement;

/**
 * IMeasurable is the common contract for every unit category (length,
 * weight, volume, temperature). Any enum implementing it can be used with
 * {@link Quantity}.
 *
 * @author Navya Raj
 */
public interface IMeasurable {

    // by default every unit category supports arithmetic
    SupportsArithmetic supportsArithmetic = () -> true;

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

    /**
     * @return true if add, subtract and divide are allowed for this unit
     */
    default boolean supportsArithmetic() {
        return supportsArithmetic.isSupported();
    }

    /**
     * Validates that the given operation is allowed for this unit. The
     * default does nothing, units like TemperatureUnit override it to
     * reject operations that make no sense for them.
     *
     * @param operation name of the operation, e.g. "ADD"
     */
    default void validateOperationSupport(String operation) {
        // all operations are supported by default
    }
}
