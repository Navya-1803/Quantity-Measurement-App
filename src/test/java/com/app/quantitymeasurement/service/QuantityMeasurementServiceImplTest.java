package com.app.quantitymeasurement.service;

import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class QuantityMeasurementServiceImplTest {

    private static final double DELTA = 1e-6;

    private QuantityMeasurementRepository repository;
    private QuantityMeasurementServiceImpl service;

    private final QuantityDTO oneFoot = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
    private final QuantityDTO twelveInches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

    @BeforeEach
    void setUp() {
        repository = mock(QuantityMeasurementRepository.class);
        // the mocked repository returns whatever entity is saved
        when(repository.save(any(QuantityMeasurementEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service = new QuantityMeasurementServiceImpl(repository);
    }

    @Test
    void testService_CompareEquality_DifferentUnit_Success() {
        QuantityMeasurementDTO result = service.compare(oneFoot, twelveInches);
        assertEquals("true", result.getResultString());
        assertEquals("compare", result.getOperation());
        verify(repository).save(any(QuantityMeasurementEntity.class));
    }

    @Test
    void testService_CompareEquality_CrossCategory_Error() {
        assertThrows(QuantityMeasurementException.class, () -> service.compare(oneFoot, new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)));
        verify(repository).save(argThat(QuantityMeasurementEntity::isError));
    }

    @Test
    void testService_Convert_Success() {
        QuantityMeasurementDTO result = service.convert(oneFoot, new QuantityDTO(0.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(12.0, result.getResultValue(), DELTA);
        assertEquals("INCHES", result.getResultUnit());
    }

    @Test
    void testService_Convert_Temperature() {
        QuantityMeasurementDTO result = service.convert(new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));
        assertEquals(212.0, result.getResultValue(), DELTA);
    }

    @Test
    void testService_Add_Success() {
        QuantityMeasurementDTO result = service.add(oneFoot, twelveInches);
        assertEquals(2.0, result.getResultValue(), DELTA);
        assertEquals("FEET", result.getResultUnit());
    }

    @Test
    void testService_Add_WithTargetUnit() {
        QuantityMeasurementDTO result = service.add(new QuantityDTO(10.0, QuantityDTO.WeightUnit.KILOGRAM),
                new QuantityDTO(5000.0, QuantityDTO.WeightUnit.GRAM), new QuantityDTO(0.0, QuantityDTO.WeightUnit.GRAM));
        assertEquals(15000.0, result.getResultValue(), DELTA);
    }

    @Test
    void testService_Add_UnsupportedOperation_Error() {
        QuantityDTO celsius = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        QuantityMeasurementException exception = assertThrows(QuantityMeasurementException.class, () -> service.add(celsius, celsius));
        assertInstanceOf(UnsupportedOperationException.class, exception.getCause());
    }

    @Test
    void testService_Add_CrossCategory_ErrorMessage() {
        QuantityMeasurementException exception = assertThrows(QuantityMeasurementException.class,
                () -> service.add(oneFoot, new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM)));
        assertEquals("add Error: Cannot perform arithmetic between different measurement categories: LengthUnit and WeightUnit",
                exception.getMessage());
    }

    @Test
    void testService_Subtract_Success() {
        QuantityMeasurementDTO result = service.subtract(new QuantityDTO(10.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(6.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(9.5, result.getResultValue(), DELTA);
    }

    @Test
    void testService_Subtract_WithTargetUnit() {
        QuantityMeasurementDTO result = service.subtract(new QuantityDTO(5.0, QuantityDTO.VolumeUnit.LITRE),
                new QuantityDTO(2.0, QuantityDTO.VolumeUnit.LITRE), new QuantityDTO(0.0, QuantityDTO.VolumeUnit.MILLILITRE));
        assertEquals(3000.0, result.getResultValue(), DELTA);
    }

    @Test
    void testService_Divide_Success() {
        assertEquals(1.0, service.divide(new QuantityDTO(24.0, QuantityDTO.LengthUnit.INCHES),
                new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET)).getResultValue(), DELTA);
    }

    @Test
    void testService_Divide_ByZero_Error() {
        assertThrows(ArithmeticException.class, () -> service.divide(oneFoot, new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET)));
        verify(repository).save(argThat(QuantityMeasurementEntity::isError));
    }

    @Test
    void testService_InvalidUnit_Error() {
        assertThrows(QuantityMeasurementException.class, () -> service.compare(oneFoot, new QuantityDTO(1.0, "METRE", "LengthUnit")));
    }

    @Test
    void testService_GetOperationHistory_MapsOperationName() {
        when(repository.findByOperation("add")).thenReturn(List.of(new QuantityMeasurementEntity()));
        assertEquals(1, service.getOperationHistory("ADD").size());
        verify(repository).findByOperation("add");
    }

    @Test
    void testService_GetOperationHistory_InvalidOperation() {
        assertThrows(IllegalArgumentException.class, () -> service.getOperationHistory("MULTIPLY"));
    }

    @Test
    void testService_GetMeasurementsByType() {
        when(repository.findByThisMeasurementType("LengthUnit")).thenReturn(List.of(new QuantityMeasurementEntity(), new QuantityMeasurementEntity()));
        assertEquals(2, service.getMeasurementsByType("LengthUnit").size());
    }

    @Test
    void testService_GetOperationCount() {
        when(repository.countByOperationAndIsErrorFalse("compare")).thenReturn(3L);
        assertEquals(3L, service.getOperationCount("COMPARE"));
    }

    @Test
    void testService_GetErrorHistory() {
        when(repository.findByIsErrorTrue()).thenReturn(List.of(new QuantityMeasurementEntity()));
        assertEquals(1, service.getErrorHistory().size());
    }
}
