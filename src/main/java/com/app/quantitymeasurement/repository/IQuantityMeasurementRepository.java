package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import java.util.List;

/**
 * Contract for storing and reading quantity measurement records. The
 * service only depends on this interface, so the cache and the database
 * implementations can be swapped freely.
 *
 * @author Navya Raj
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();

    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);

    List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType);

    int getTotalCount();

    void deleteAll();

    /**
     * @return connection pool details, only meaningful for database repositories
     */
    default String getPoolStatistics() {
        return "Not applicable for " + getClass().getSimpleName();
    }

    /**
     * Releases any resources (like database connections) held by the repository.
     */
    default void releaseResources() {
        // nothing to release by default
    }
}
