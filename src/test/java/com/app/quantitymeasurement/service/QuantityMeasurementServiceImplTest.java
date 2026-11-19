package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.repository.IQuantityMeasurementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class QuantityMeasurementServiceImplTest {

    private static final double DELTA = 1e-6;

    private IQuantityMeasurementRepository repository;
    private QuantityMeasurementServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(IQuantityMeasurementRepository.class);
        service = new QuantityMeasurementServiceImpl(repository);
    }

    private static QuantityDTO length(double value, QuantityDTO.LengthUnit unit) {
        return new QuantityDTO(value, unit);
    }

    @Test
    void testService_CompareEquality_SameUnit_Success() {
        assertTrue(service.compare(length(1.0, QuantityDTO.LengthUnit.FEET), length(1.0, QuantityDTO.LengthUnit.FEET)));
        verify(repository).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {
        assertTrue(service.compare(length(1.0, QuantityDTO.LengthUnit.FEET), length(12.0, QuantityDTO.LengthUnit.INCHES)));
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {
        QuantityDTO kilogram = new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM);
        assertThrows(QuantityMeasurementException.class, () -> service.compare(length(1.0, QuantityDTO.LengthUnit.FEET), kilogram));
        verify(repository).save(argThat(QuantityMeasurementEntity::isError));
    }

    @Test
    void testService_Convert_Success() {
        QuantityDTO result = service.convert(length(1.0, QuantityDTO.LengthUnit.FEET), length(0.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(12.0, result.getValue(), DELTA);
        assertEquals("INCHES", result.getUnit());
    }

    @Test
    void testService_Convert_Temperature() {
        QuantityDTO result = service.convert(new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));
        assertEquals(212.0, result.getValue(), DELTA);
    }

    @Test
    void testService_Add_Success() {
        QuantityDTO result = service.add(length(1.0, QuantityDTO.LengthUnit.FEET), length(12.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(2.0, result.getValue(), DELTA);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    void testService_Add_WithTargetUnit() {
        QuantityDTO result = service.add(new QuantityDTO(10.0, QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(5000.0, QuantityDTO.WeightUnit.GRAM), new QuantityDTO(0.0, QuantityDTO.WeightUnit.GRAM));
        assertEquals(15000.0, result.getValue(), DELTA);
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {
        QuantityDTO celsius = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityMeasurementException exception = assertThrows(QuantityMeasurementException.class, () -> service.add(celsius, celsius));
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }

    @Test
    void testService_Subtract_Success() {
        QuantityDTO result = service.subtract(length(10.0, QuantityDTO.LengthUnit.FEET), length(6.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(9.5, result.getValue(), DELTA);
    }

    @Test
    void testService_Subtract_WithTargetUnit() {
        QuantityDTO result = service.subtract(new QuantityDTO(5.0, QuantityDTO.VolumeUnit.LITRE),
                new QuantityDTO(2.0, QuantityDTO.VolumeUnit.LITRE), new QuantityDTO(0.0, QuantityDTO.VolumeUnit.MILLILITRE));
        assertEquals(3000.0, result.getValue(), DELTA);
    }

    @Test
    void testService_Divide_Success() {
        assertEquals(1.0, service.divide(length(24.0, QuantityDTO.LengthUnit.INCHES), length(2.0, QuantityDTO.LengthUnit.FEET)), DELTA);
    }

    @Test
    void testService_Divide_ByZero_Error() {
        QuantityMeasurementException exception = assertThrows(QuantityMeasurementException.class,
                () -> service.divide(length(10.0, QuantityDTO.LengthUnit.FEET), length(0.0, QuantityDTO.LengthUnit.FEET)));
        assertInstanceOf(ArithmeticException.class, exception.getCause());
    }

    @Test
    void testService_AllMeasurementCategories() {
        assertTrue(service.compare(new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM), new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM)));
        assertTrue(service.compare(new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE), new QuantityDTO(1000.0, QuantityDTO.VolumeUnit.MILLILITRE)));
        assertTrue(service.compare(new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS), new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT)));
    }

    @Test
    void testService_ValidationConsistency() {
        QuantityDTO invalidUnit = new QuantityDTO(1.0, "METRE", "LengthUnit");
        QuantityDTO invalidType = new QuantityDTO(1.0, "FEET", "AreaUnit");
        QuantityDTO feet = length(1.0, QuantityDTO.LengthUnit.FEET);
        assertThrows(QuantityMeasurementException.class, () -> service.compare(feet, invalidUnit));
        assertThrows(QuantityMeasurementException.class, () -> service.add(feet, invalidType));
    }

    @Test
    void testService_ExceptionHandling_AllOperations() {
        QuantityDTO feet = length(1.0, QuantityDTO.LengthUnit.FEET);
        QuantityDTO gram = new QuantityDTO(1.0, QuantityDTO.WeightUnit.GRAM);
        assertThrows(QuantityMeasurementException.class, () -> service.convert(feet, gram));
        assertThrows(QuantityMeasurementException.class, () -> service.add(feet, gram));
        assertThrows(QuantityMeasurementException.class, () -> service.subtract(feet, gram));
        assertThrows(QuantityMeasurementException.class, () -> service.divide(feet, gram));
    }

    @Test
    void testService_ErrorMessage_Format() {
        QuantityMeasurementException exception = assertThrows(QuantityMeasurementException.class,
                () -> service.add(length(1.0, QuantityDTO.LengthUnit.FEET), new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)));
        assertEquals("add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit",
                exception.getMessage());
    }

    @Test
    void testService_NullEntity_Rejection() {
        assertThrows(QuantityMeasurementException.class, () -> service.compare(null, length(1.0, QuantityDTO.LengthUnit.FEET)));
    }

    @Test
    void testService_NullRepository_Rejected() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityMeasurementServiceImpl(null));
    }

    @Test
    void testService_AllUnitImplementations() {
        for (QuantityDTO.LengthUnit unit : QuantityDTO.LengthUnit.values()) {
            assertTrue(service.compare(new QuantityDTO(1.0, unit), new QuantityDTO(1.0, unit)));
        }
        for (QuantityDTO.WeightUnit unit : QuantityDTO.WeightUnit.values()) {
            assertTrue(service.compare(new QuantityDTO(1.0, unit), new QuantityDTO(1.0, unit)));
        }
    }

    @Test
    void testService_SavesOneRecordPerOperation() {
        service.add(length(1.0, QuantityDTO.LengthUnit.FEET), length(1.0, QuantityDTO.LengthUnit.FEET));
        service.divide(length(1.0, QuantityDTO.LengthUnit.FEET), length(1.0, QuantityDTO.LengthUnit.FEET));
        verify(repository, times(2)).save(any(QuantityMeasurementEntity.class));
    }
}
