package com.app.quantitymeasurement.model;

import com.app.quantitymeasurement.unit.IMeasurable;

/**
 * QuantityModel is the internal representation of a quantity used inside
 * the service layer. Unlike QuantityDTO it holds a real unit object.
 *
 * @param <U> unit type implementing IMeasurable
 * @author Navya Raj
 */
public class QuantityModel<U extends IMeasurable> {

    private final double value;
    private final U unit;

    public QuantityModel(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return "Quantity(" + value + ", " + (unit == null ? null : unit.getUnitName()) + ")";
    }
}
