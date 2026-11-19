package com.app.quantitymeasurement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementCacheRepositoryTest {

    @TempDir
    Path tempDir;

    private QuantityMeasurementEntity sampleEntity(double value) {
        QuantityModel<IMeasurable> quantity = new QuantityModel<>(value, LengthUnit.FEET);
        return new QuantityMeasurementEntity(quantity, "convert", new QuantityModel<>(value * 12, LengthUnit.INCHES));
    }

    @Test
    void testSave_AddsToCache() {
        QuantityMeasurementCacheRepository repository = new QuantityMeasurementCacheRepository(tempDir.resolve("repo.ser").toString());
        repository.save(sampleEntity(1.0));
        repository.save(sampleEntity(2.0));
        assertEquals(2, repository.getAllMeasurements().size());
    }

    @Test
    void testSave_NullEntity_Rejected() {
        QuantityMeasurementCacheRepository repository = new QuantityMeasurementCacheRepository(tempDir.resolve("repo.ser").toString());
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void testPersistence_ReloadedAfterRestart() {
        String file = tempDir.resolve("repo.ser").toString();
        QuantityMeasurementCacheRepository first = new QuantityMeasurementCacheRepository(file);
        first.save(sampleEntity(1.0));
        first.save(sampleEntity(2.0));
        first.save(sampleEntity(3.0));

        // a new repository on the same file reads every appended record back
        QuantityMeasurementCacheRepository reloaded = new QuantityMeasurementCacheRepository(file);
        assertEquals(3, reloaded.getAllMeasurements().size());
        assertEquals(36.0, reloaded.getAllMeasurements().get(2).getResultValue());
    }

    @Test
    void testGetAllMeasurements_ReturnsReadOnlyCopy() {
        QuantityMeasurementCacheRepository repository = new QuantityMeasurementCacheRepository(tempDir.resolve("repo.ser").toString());
        repository.save(sampleEntity(1.0));
        assertThrows(UnsupportedOperationException.class, () -> repository.getAllMeasurements().clear());
    }

    @Test
    void testGetInstance_IsSingleton() {
        assertSame(QuantityMeasurementCacheRepository.getInstance(), QuantityMeasurementCacheRepository.getInstance());
    }
}
