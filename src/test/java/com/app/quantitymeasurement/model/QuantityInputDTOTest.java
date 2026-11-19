package com.app.quantitymeasurement.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityInputDTOTest {

    private final QuantityDTO feet = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
    private final QuantityDTO inches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

    @Test
    void testTwoArgumentConstructor_NoTarget() {
        QuantityInputDTO input = new QuantityInputDTO(feet, inches);
        assertSame(feet, input.getThisQuantityDTO());
        assertSame(inches, input.getThatQuantityDTO());
        assertNull(input.getTargetQuantityDTO());
    }

    @Test
    void testValidation_NestedDTOsAreValidated() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            assertTrue(validator.validate(new QuantityInputDTO(feet, inches)).isEmpty());
            assertFalse(validator.validate(new QuantityInputDTO(feet, new QuantityDTO(1.0, "INCHE", "LengthUnit"))).isEmpty());
            assertFalse(validator.validate(new QuantityInputDTO(feet, null)).isEmpty());
        }
    }
}
