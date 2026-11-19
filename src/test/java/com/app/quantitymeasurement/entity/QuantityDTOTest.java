package com.app.quantitymeasurement.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityDTOTest {

    @Test
    void testConstructor_WithUnitEnum() {
        QuantityDTO dto = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertEquals(1.0, dto.getValue());
        assertEquals("FEET", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testConstructor_WithStrings() {
        QuantityDTO dto = new QuantityDTO(2.0, "GRAM", "WeightUnit");
        assertEquals("GRAM", dto.getUnit());
        assertEquals("WeightUnit", dto.getMeasurementType());
    }

    @Test
    void testSetters() {
        QuantityDTO dto = new QuantityDTO();
        dto.setValue(3.0);
        dto.setUnit("LITRE");
        dto.setMeasurementType("VolumeUnit");
        assertEquals(3.0, dto.getValue());
        assertEquals("LITRE", dto.getUnit());
        assertEquals("VolumeUnit", dto.getMeasurementType());
    }

    @Test
    void testNestedEnums_MeasurementTypes() {
        assertEquals("LengthUnit", QuantityDTO.LengthUnit.INCHES.getMeasurementType());
        assertEquals("WeightUnit", QuantityDTO.WeightUnit.POUND.getMeasurementType());
        assertEquals("VolumeUnit", QuantityDTO.VolumeUnit.GALLON.getMeasurementType());
        assertEquals("TemperatureUnit", QuantityDTO.TemperatureUnit.KELVIN.getMeasurementType());
    }

    @Test
    void testNestedEnums_MatchServiceUnits() {
        // every DTO unit must exist in the matching service layer enum
        for (QuantityDTO.LengthUnit unit : QuantityDTO.LengthUnit.values()) {
            assertNotNull(com.app.quantitymeasurement.unit.LengthUnit.valueOf(unit.getUnitName()));
        }
        for (QuantityDTO.TemperatureUnit unit : QuantityDTO.TemperatureUnit.values()) {
            assertNotNull(com.app.quantitymeasurement.unit.TemperatureUnit.valueOf(unit.getUnitName()));
        }
    }

    @Test
    void testToString() {
        assertEquals("QuantityDTO(1.0, FEET, LengthUnit)", new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET).toString());
    }
}
