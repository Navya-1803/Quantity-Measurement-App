package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.util.ApplicationConfig;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryFactoryTest {

    @AfterEach
    void clearOverride() {
        System.clearProperty("repository.type");
    }

    @Test
    void testRepositoryFactory_CreateCacheRepository() {
        System.setProperty("repository.type", "cache");
        IQuantityMeasurementRepository repository = RepositoryFactory.createRepository(new ApplicationConfig("application-test.properties"));
        assertInstanceOf(QuantityMeasurementCacheRepository.class, repository);
    }

    @Test
    void testRepositoryFactory_CreateDatabaseRepository() {
        IQuantityMeasurementRepository repository = RepositoryFactory.createRepository(new ApplicationConfig("application-test.properties"));
        assertInstanceOf(QuantityMeasurementDatabaseRepository.class, repository);
        repository.releaseResources();
    }

    @Test
    void testRepositoryFactory_UnknownType() {
        System.setProperty("repository.type", "mongodb");
        assertThrows(IllegalArgumentException.class, () -> RepositoryFactory.createRepository(new ApplicationConfig("application-test.properties")));
    }

    @Test
    void testRepositoryFactory_NullConfig() {
        assertThrows(IllegalArgumentException.class, () -> RepositoryFactory.createRepository(null));
    }
}
