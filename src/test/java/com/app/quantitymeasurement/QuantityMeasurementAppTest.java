package com.app.quantitymeasurement;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;

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
        // application.properties selects the database repository
        assertInstanceOf(QuantityMeasurementDatabaseRepository.class, app.getRepository());
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

    @Test
    void testReportAndDeleteAllMeasurements() {
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();
        app.getController().performComparison(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        assertDoesNotThrow(app::reportAllMeasurements);
        app.deleteAllMeasurements();
        assertEquals(0, app.getRepository().getTotalCount());
    }
}
