package com.app.quantitymeasurement;

import com.app.quantitymeasurement.controller.QuantityMeasurementController;
import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.app.quantitymeasurement.repository.QuantityMeasurementDatabaseRepository;
import com.app.quantitymeasurement.service.QuantityMeasurementServiceImpl;
import com.app.quantitymeasurement.util.ConnectionPool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementIntegrationTest {

    private QuantityMeasurementDatabaseRepository repository;
    private QuantityMeasurementController controller;

    @BeforeEach
    void setUp() {
        ConnectionPool pool = new ConnectionPool("org.h2.Driver", "jdbc:h2:mem:integration_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1", "sa", "", 2, 500);
        repository = new QuantityMeasurementDatabaseRepository(pool, "db/schema.sql");
        controller = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(repository));
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
    }

    @Test
    void testServiceWithDatabaseRepository_Integration() {
        controller.performComparison(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET), new QuantityDTO(12.0, QuantityDTO.LengthUnit.INCHES));
        controller.performAddition(new QuantityDTO(1.0, QuantityDTO.WeightUnit.KILOGRAM), new QuantityDTO(1000.0, QuantityDTO.WeightUnit.GRAM));
        controller.performDivision(new QuantityDTO(1.0, QuantityDTO.LengthUnit.FEET), new QuantityDTO(0.0, QuantityDTO.LengthUnit.FEET));

        assertEquals(3, repository.getTotalCount());
        assertEquals(1, repository.getMeasurementsByOperation("add").size());
        assertTrue(repository.getMeasurementsByOperation("divide").get(0).isError());
    }

    @Test
    void testServiceWithCacheRepository_Integration() throws Exception {
        Path file = File.createTempFile("integration", ".ser").toPath();
        file.toFile().delete();
        var cacheRepository = new QuantityMeasurementCacheRepositoryAccess(file.toString()).create();
        QuantityMeasurementController cacheController = new QuantityMeasurementController(new QuantityMeasurementServiceImpl(cacheRepository));
        cacheController.performConversion(new QuantityDTO(1.0, QuantityDTO.VolumeUnit.LITRE), new QuantityDTO(0.0, QuantityDTO.VolumeUnit.MILLILITRE));
        assertEquals(1, cacheRepository.getTotalCount());
        file.toFile().delete();
    }

    @Test
    void testParameterizedQuery_DateTimeHandling() {
        controller.performComparison(new QuantityDTO(0.0, QuantityDTO.TemperatureUnit.CELSIUS), new QuantityDTO(32.0, QuantityDTO.TemperatureUnit.FAHRENHEIT));
        // the record is stored and read back, created_at is filled by the database
        assertEquals("true", repository.getMeasurementsByMeasurementType("TemperatureUnit").get(0).getResultString());
    }

    // small helper because the file based constructor is package-private
    private record QuantityMeasurementCacheRepositoryAccess(String file) {
        QuantityMeasurementCacheRepository create() {
            return com.app.quantitymeasurement.repository.CacheRepositoryTestFactory.create(file);
        }
    }
}
