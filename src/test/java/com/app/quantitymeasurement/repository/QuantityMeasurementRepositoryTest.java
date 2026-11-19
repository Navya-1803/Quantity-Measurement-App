package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.model.QuantityModel;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QuantityMeasurementRepositoryTest {

    @Autowired
    private QuantityMeasurementRepository repository;

    private final QuantityModel<IMeasurable> feet = new QuantityModel<>(1.0, LengthUnit.FEET);
    private final QuantityModel<IMeasurable> inches = new QuantityModel<>(12.0, LengthUnit.INCHES);

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.save(new QuantityMeasurementEntity(feet, inches, "compare", "true"));
        repository.save(new QuantityMeasurementEntity(feet, inches, "add", new QuantityModel<IMeasurable>(2.0, LengthUnit.FEET)));
        QuantityModel<IMeasurable> kg = new QuantityModel<>(1.0, WeightUnit.KILOGRAM);
        repository.save(new QuantityMeasurementEntity(feet, kg, "add", "Cannot perform arithmetic", true));
    }

    @Test
    void testH2DatabasePersistence() {
        assertEquals(3, repository.count());
        QuantityMeasurementEntity saved = repository.findAll().get(0);
        assertNotNull(saved.getId());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void testJPARepositoryFindByOperation() {
        assertEquals(2, repository.findByOperation("add").size());
        assertEquals(1, repository.findByOperation("compare").size());
    }

    @Test
    void testFindByMeasurementType() {
        assertEquals(3, repository.findByThisMeasurementType("LengthUnit").size());
        assertTrue(repository.findByThisMeasurementType("VolumeUnit").isEmpty());
    }

    @Test
    void testJPARepositoryCustomQuery() {
        assertEquals(1, repository.findSuccessfulOperations("add").size());
    }

    @Test
    void testCountAndErrorQueries() {
        assertEquals(1, repository.countByOperationAndIsErrorFalse("add"));
        assertEquals(1, repository.findByIsErrorTrue().size());
    }

    @Test
    void testFindByCreatedAtAfter() {
        assertEquals(3, repository.findByCreatedAtAfter(LocalDateTime.now().minusMinutes(1)).size());
        assertTrue(repository.findByCreatedAtAfter(LocalDateTime.now().plusMinutes(1)).isEmpty());
    }
}
