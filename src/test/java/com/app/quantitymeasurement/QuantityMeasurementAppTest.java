package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementAppTest {

    @Test
    void testGetInstance_IsSingleton() {
        assertSame(QuantityMeasurementApp.getInstance(), QuantityMeasurementApp.getInstance());
    }

    @Test
    void testApp_CreatesAllLayers() {
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();
        assertNotNull(app.getController());
        assertInstanceOf(QuantityMeasurementCacheRepository.class, app.getRepository());
    }

    @Test
    void testIntegration_EndToEnd_LengthAddition() {
        QuantityDTO result = QuantityMeasurementApp.getInstance().getController().performAddition(
                new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET), new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertEquals(2.0, result.getValue(), 1e-6);
    }

    @Test
    void testMain_RunsWithoutException() {
        assertDoesNotThrow(() -> QuantityMeasurementApp.main(new String[]{}));
    }
}
