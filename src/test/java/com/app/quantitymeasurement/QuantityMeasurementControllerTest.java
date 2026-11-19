package com.app.quantitymeasurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QuantityMeasurementControllerTest {

    private IQuantityMeasurementService service;
    private QuantityMeasurementController controller;

    private final QuantityDTO oneFoot = new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET);
    private final QuantityDTO twelveInches = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);

    @BeforeEach
    void setUp() {
        service = mock(IQuantityMeasurementService.class);
        controller = new QuantityMeasurementController(service);
    }

    @Test
    void testController_DemonstrateEquality_Success() {
        when(service.compare(oneFoot, twelveInches)).thenReturn(true);
        assertTrue(controller.performComparison(oneFoot, twelveInches));
    }

    @Test
    void testController_DemonstrateConversion_Success() {
        QuantityDTO expected = new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES);
        when(service.convert(any(), any())).thenReturn(expected);
        assertSame(expected, controller.performConversion(oneFoot, twelveInches));
    }

    @Test
    void testController_DemonstrateAddition_Success() {
        QuantityDTO expected = new QuantityDTO(2.0, QuantityDTO.LengthUnit.FEET);
        when(service.add(oneFoot, twelveInches)).thenReturn(expected);
        assertSame(expected, controller.performAddition(oneFoot, twelveInches));
    }

    @Test
    void testController_DemonstrateAddition_Error() {
        when(service.add(any(), any())).thenThrow(new QuantityMeasurementException("add Error: failed"));
        assertNull(controller.performAddition(oneFoot, twelveInches));
    }

    @Test
    void testController_Subtraction_Success() {
        QuantityDTO expected = new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET);
        when(service.subtract(oneFoot, twelveInches, oneFoot)).thenReturn(expected);
        assertSame(expected, controller.performSubtraction(oneFoot, twelveInches, oneFoot));
    }

    @Test
    void testController_Division_Error() {
        when(service.divide(any(), any())).thenThrow(new QuantityMeasurementException("divide Error: Cannot divide by zero quantity"));
        assertNull(controller.performDivision(oneFoot, twelveInches));
    }

    @Test
    void testController_DisplayResult_Success() {
        assertEquals("Addition -> Input: a + b | Output: 2.0", controller.displayResult("Addition", "a + b", 2.0));
    }

    @Test
    void testController_DisplayResult_Error() {
        assertEquals("Addition -> Error: boom", controller.displayError("Addition", new QuantityMeasurementException("boom")));
    }

    @Test
    void testController_NullService_Prevention() {
        assertThrows(IllegalArgumentException.class, () -> new QuantityMeasurementController(null));
    }

    @Test
    void testDataFlow_ControllerToService() {
        controller.performDivision(oneFoot, twelveInches);
        verify(service).divide(oneFoot, twelveInches);
    }

    @Test
    void testController_AllOperations() {
        // controller works end to end with the real service and a mocked repository
        QuantityMeasurementController realController = new QuantityMeasurementController(
                new QuantityMeasurementServiceImpl(mock(IQuantityMeasurementRepository.class)));
        assertTrue(realController.performComparison(oneFoot, twelveInches));
        assertEquals(12.0, realController.performConversion(oneFoot, twelveInches).getValue(), 1e-6);
        assertEquals(2.0, realController.performAddition(oneFoot, twelveInches).getValue(), 1e-6);
        assertEquals(0.0, realController.performSubtraction(oneFoot, twelveInches).getValue(), 1e-6);
        assertEquals(1.0, realController.performDivision(oneFoot, twelveInches), 1e-6);
    }

    @Test
    void testIntegration_EndToEnd_TemperatureUnsupported() {
        QuantityMeasurementController realController = new QuantityMeasurementController(
                new QuantityMeasurementServiceImpl(mock(IQuantityMeasurementRepository.class)));
        QuantityDTO celsius = new QuantityDTO(100.0, QuantityDTO.TemperatureUnit.CELSIUS);
        assertNull(realController.performAddition(celsius, celsius));
    }
}
