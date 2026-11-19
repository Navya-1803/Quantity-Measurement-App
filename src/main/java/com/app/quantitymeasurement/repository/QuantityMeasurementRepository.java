package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for measurement records. Basic CRUD comes from
 * JpaRepository, the query methods below are generated from their names
 * or defined with @Query.
 *
 * @author Navya Raj
 */
@Repository
public interface QuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperation(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementType(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime dateTime);

    List<QuantityMeasurementEntity> findByIsErrorTrue();

    long countByOperationAndIsErrorFalse(String operation);

    // successful records of one operation, newest first
    @Query("SELECT e FROM QuantityMeasurementEntity e WHERE e.operation = :operation AND e.isError = false ORDER BY e.createdAt DESC")
    List<QuantityMeasurementEntity> findSuccessfulOperations(@Param("operation") String operation);
}
