package com.app.quantitymeasurement.repository;

/**
 * Test helper that exposes the package-private file based constructor of
 * the cache repository to tests in other packages.
 */
public final class CacheRepositoryTestFactory {

    private CacheRepositoryTestFactory() {
    }

    public static QuantityMeasurementCacheRepository create(String file) {
        return new QuantityMeasurementCacheRepository(file);
    }
}
