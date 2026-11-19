package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.entity.QuantityModel;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.unit.IMeasurable;
import com.app.quantitymeasurement.unit.LengthUnit;
import com.app.quantitymeasurement.unit.WeightUnit;
import com.app.quantitymeasurement.util.ConnectionPool;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class QuantityMeasurementDatabaseRepositoryTest {

    private ConnectionPool pool;
    private QuantityMeasurementDatabaseRepository repository;

    @BeforeEach
    void setUp() {
        // every test gets its own in-memory database
        String url = "jdbc:h2:mem:repotest_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1";
        pool = new ConnectionPool("org.h2.Driver", url, "sa", "", 3, 500);
        repository = new QuantityMeasurementDatabaseRepository(pool, "db/schema.sql");
    }

    @AfterEach
    void tearDown() {
        repository.releaseResources();
    }

    private QuantityMeasurementEntity comparison() {
        QuantityModel<IMeasurable> feet = new QuantityModel<>(1.0, LengthUnit.FEET);
        QuantityModel<IMeasurable> inches = new QuantityModel<>(12.0, LengthUnit.INCHES);
        return new QuantityMeasurementEntity(feet, inches, "compare", "true");
    }

    private QuantityMeasurementEntity weightAddition() {
        QuantityModel<IMeasurable> kg = new QuantityModel<>(1.0, WeightUnit.KILOGRAM);
        QuantityModel<IMeasurable> g = new QuantityModel<>(1000.0, WeightUnit.GRAM);
        return new QuantityMeasurementEntity(kg, g, "add", new QuantityModel<IMeasurable>(2.0, WeightUnit.KILOGRAM));
    }

    @Test
    void testDatabaseSchema_TablesCreated() throws Exception {
        Connection connection = pool.getConnection();
        try (ResultSet rs = connection.getMetaData().getTables(null, null, "QUANTITY_MEASUREMENT_ENTITY", null)) {
            assertTrue(rs.next());
        } finally {
            pool.releaseConnection(connection);
        }
    }

    @Test
    void testDatabaseRepository_SaveEntity() {
        repository.save(comparison());
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepository_RetrieveAllMeasurements() {
        repository.save(comparison());
        repository.save(weightAddition());
        List<QuantityMeasurementEntity> all = repository.getAllMeasurements();
        assertEquals(2, all.size());
        assertEquals("compare", all.get(0).getOperation());
        assertEquals("true", all.get(0).getResultString());
        assertEquals(2.0, all.get(1).getResultValue());
        assertEquals("KILOGRAM", all.get(1).getResultUnit());
    }

    @Test
    void testDatabaseRepository_QueryByOperation() {
        repository.save(comparison());
        repository.save(weightAddition());
        assertEquals(1, repository.getMeasurementsByOperation("add").size());
        assertEquals(1, repository.getMeasurementsByOperation("COMPARE").size());
    }

    @Test
    void testDatabaseRepository_QueryByMeasurementType() {
        repository.save(comparison());
        repository.save(weightAddition());
        assertEquals(1, repository.getMeasurementsByMeasurementType("WeightUnit").size());
        assertEquals(0, repository.getMeasurementsByMeasurementType("VolumeUnit").size());
    }

    @Test
    void testDatabaseRepository_CountMeasurements() {
        assertEquals(0, repository.getTotalCount());
        repository.save(comparison());
        repository.save(comparison());
        assertEquals(2, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepository_DeleteAll() {
        repository.save(comparison());
        repository.deleteAll();
        assertEquals(0, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepository_ErrorEntity() {
        QuantityModel<IMeasurable> feet = new QuantityModel<>(1.0, LengthUnit.FEET);
        repository.save(new QuantityMeasurementEntity(feet, null, "add", "add Error: failed", true));
        QuantityMeasurementEntity saved = repository.getAllMeasurements().get(0);
        assertTrue(saved.isError());
        assertEquals("add Error: failed", saved.getErrorMessage());
    }

    @Test
    void testSQLInjectionPrevention() {
        repository.save(comparison());
        // the malicious text is treated as a plain value, the table is untouched
        assertTrue(repository.getMeasurementsByOperation("compare' OR '1'='1").isEmpty());
        assertTrue(repository.getMeasurementsByMeasurementType("x'; DROP TABLE quantity_measurement_entity; --").isEmpty());
        assertEquals(1, repository.getTotalCount());
    }

    @Test
    void testTransactionRollback_OnError() {
        // the second entity is null, so the whole batch must be rolled back
        List<QuantityMeasurementEntity> batch = Arrays.asList(comparison(), null);
        assertThrows(RuntimeException.class, () -> repository.saveAll(batch));
        assertEquals(0, repository.getTotalCount());
    }

    @Test
    void testBatchInsert_MultipleEntities() {
        repository.saveAll(List.of(comparison(), weightAddition(), comparison()));
        assertEquals(3, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepositoryPoolStatistics() {
        assertEquals("Pool[total=3, active=0, idle=3]", repository.getPoolStatistics());
    }

    @Test
    void testDatabaseRepository_LargeDataSet() {
        List<QuantityMeasurementEntity> entities = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            entities.add(comparison());
        }
        repository.saveAll(entities);
        assertEquals(500, repository.getTotalCount());
    }

    @Test
    void testDatabaseRepository_ConcurrentAccess() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 20; i++) {
            executor.submit(() -> repository.save(comparison()));
        }
        executor.shutdown();
        assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
        assertEquals(20, repository.getTotalCount());
    }

    @Test
    void testSaveNullEntity_Rejected() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void testMissingSchema_Throws() {
        ConnectionPool otherPool = new ConnectionPool("org.h2.Driver", "jdbc:h2:mem:missing_schema", "sa", "", 1, 200);
        assertThrows(DatabaseException.class, () -> new QuantityMeasurementDatabaseRepository(otherPool, "db/missing.sql"));
        otherPool.closeAll();
    }

    @Test
    void testH2TestDatabase_IsolationBetweenTests() {
        // a fresh database is created in setUp, so nothing from other tests is visible
        assertEquals(0, repository.getTotalCount());
    }
}
