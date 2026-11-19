package com.app.quantitymeasurement.entity;

import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.TemperatureUnit;
import com.app.quantitymeasurement.unit.WeightUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityModelTest {

    @Test
    void testModel_HoldsValueAndUnit() {
        QuantityModel<LengthUnit> model = new QuantityModel<>(2.5, LengthUnit.YARDS);
        assertEquals(2.5, model.getValue());
        assertEquals(LengthUnit.YARDS, model.getUnit());
    }

    @Test
    void testModel_WorksWithAnyCategory() {
        QuantityModel<IMeasurable> model = new QuantityModel<>(100.0, TemperatureUnit.CELSIUS);
        assertEquals("TemperatureUnit", model.getUnit().getMeasurementType());
    }

    @Test
    void testToString() {
        assertEquals("Quantity(1.0, KILOGRAM)", new QuantityModel<>(1.0, WeightUnit.KILOGRAM).toString());
    }
}
