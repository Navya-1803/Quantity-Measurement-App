package com.app.quantitymeasurement.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class QuantityDTOTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeValidator() {
        factory.close();
    }

    @Test
    void testConstructor_WithUnitEnum() {
        QuantityDTO dto = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
        assertEquals(1.0, dto.getValue().doubleValue());
        assertEquals("FEET", dto.getUnit());
        assertEquals("LengthUnit", dto.getMeasurementType());
    }

    @Test
    void testNestedEnums_MeasurementTypes() {
        assertEquals("WeightUnit", QuantityDTO.WeightUnit.POUND.getMeasurementType());
        assertEquals("VolumeUnit", QuantityDTO.VolumeUnit.GALLON.getMeasurementType());
        assertEquals("TemperatureUnit", QuantityDTO.TemperatureUnit.KELVIN.getMeasurementType());
    }

    @Test
    void testNestedEnums_MatchServiceUnits() {
        for (QuantityDTO.LengthUnit unit : QuantityDTO.LengthUnit.values()) {
            assertNotNull(com.app.quantitymeasurement.unit.LengthUnit.valueOf(unit.getUnitName()));
        }
        for (QuantityDTO.TemperatureUnit unit : QuantityDTO.TemperatureUnit.values()) {
            assertNotNull(com.app.quantitymeasurement.unit.TemperatureUnit.valueOf(unit.getUnitName()));
        }
    }

    @Test
    void testValidation_ValidDTO() {
        assertTrue(validator.validate(new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE)).isEmpty());
    }

    @Test
    void testValidation_NullValue() {
        Set<ConstraintViolation<QuantityDTO>> violations = validator.validate(new QuantityDTO(null, "FEET", "LengthUnit"));
        assertEquals("Value cannot be null", violations.iterator().next().getMessage());
    }

    @Test
    void testValidation_EmptyUnit() {
        assertFalse(validator.validate(new QuantityDTO(1.0, "", "LengthUnit")).isEmpty());
    }

    @Test
    void testValidation_InvalidMeasurementType() {
        assertFalse(validator.validate(new QuantityDTO(1.0, "FEET", "AreaUnit")).isEmpty());
    }

    @Test
    void testValidation_UnitNotInMeasurementType() {
        Set<ConstraintViolation<QuantityDTO>> violations = validator.validate(new QuantityDTO(1.0, "INCHE", "LengthUnit"));
        assertEquals(1, violations.size());
        assertEquals("Unit must be valid for the specified measurement type", violations.iterator().next().getMessage());
    }

    @Test
    void testIsValidUnit_IgnoresCase() {
        assertTrue(new QuantityDTO(1.0, "feet", "LengthUnit").isValidUnit());
        assertFalse(new QuantityDTO(1.0, "FEET", "WeightUnit").isValidUnit());
    }
}
