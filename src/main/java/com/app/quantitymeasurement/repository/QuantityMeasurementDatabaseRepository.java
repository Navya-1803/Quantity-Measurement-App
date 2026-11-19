package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.app.quantitymeasurement.exception.DatabaseException;
import com.app.quantitymeasurement.util.ConnectionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * QuantityMeasurementDatabaseRepository stores measurement records in a
 * relational database using plain JDBC. All queries use PreparedStatement
 * parameters, which protects against SQL injection, and writes run inside
 * a transaction that is rolled back on failure.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementDatabaseRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementDatabaseRepository.class);

    private static final String TABLE = "quantity_measurement_entity";

    private static final String INSERT_SQL = "INSERT INTO " + TABLE
            + " (this_value, this_unit, this_measurement_type, that_value, that_unit, that_measurement_type,"
            + " operation, result_string, result_value, result_unit, result_measurement_type, error_message, is_error)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL = "SELECT * FROM " + TABLE + " ORDER BY id";
    private static final String SELECT_BY_OPERATION_SQL = "SELECT * FROM " + TABLE + " WHERE LOWER(operation) = LOWER(?) ORDER BY id";
    private static final String SELECT_BY_TYPE_SQL = "SELECT * FROM " + TABLE + " WHERE this_measurement_type = ? ORDER BY id";
    private static final String COUNT_SQL = "SELECT COUNT(*) FROM " + TABLE;
    private static final String DELETE_ALL_SQL = "DELETE FROM " + TABLE;

    private final ConnectionPool connectionPool;

    public QuantityMeasurementDatabaseRepository(ConnectionPool connectionPool, String schemaResource) {
        if (connectionPool == null) {
            throw new IllegalArgumentException("Connection pool cannot be null");
        }
        this.connectionPool = connectionPool;
        initializeSchema(schemaResource);
        logger.info("Database repository initialized");
    }

    // runs every statement of the schema file, tables are created only if missing
    private void initializeSchema(String schemaResource) {
        String script = readResource(schemaResource);
        Connection connection = connectionPool.getConnection();
        try (Statement statement = connection.createStatement()) {
            for (String sql : script.split(";")) {
                String cleaned = sql.replaceAll("(?m)^\\s*--.*$", "").trim();
                if (!cleaned.isEmpty()) {
                    statement.execute(cleaned);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not create database schema: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private String readResource(String resource) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                throw new DatabaseException("Schema file not found: " + resource);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DatabaseException("Could not read schema file: " + resource, e);
        }
    }

    @Override
    public void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity to save cannot be null");
        }
        Connection connection = connectionPool.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
                bindEntity(statement, entity);
                statement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                // undo the partial write so the table stays consistent
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save measurement: " + e.getMessage(), e);
        } finally {
            resetAutoCommit(connection);
            connectionPool.releaseConnection(connection);
        }
    }

    /**
     * Saves several entities in one transaction, either all are saved or none.
     */
    public void saveAll(List<QuantityMeasurementEntity> entities) {
        Connection connection = connectionPool.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL)) {
                for (QuantityMeasurementEntity entity : entities) {
                    bindEntity(statement, entity);
                    statement.addBatch();
                }
                statement.executeBatch();
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Could not save measurements: " + e.getMessage(), e);
        } finally {
            resetAutoCommit(connection);
            connectionPool.releaseConnection(connection);
        }
    }

    // fills the insert parameters from the entity
    private void bindEntity(PreparedStatement statement, QuantityMeasurementEntity entity) throws SQLException {
        if (entity == null) {
            throw new IllegalArgumentException("Entity to save cannot be null");
        }
        statement.setDouble(1, entity.getThisValue());
        statement.setString(2, entity.getThisUnit());
        statement.setString(3, entity.getThisMeasurementType());
        statement.setDouble(4, entity.getThatValue());
        statement.setString(5, entity.getThatUnit());
        statement.setString(6, entity.getThatMeasurementType());
        statement.setString(7, entity.getOperation());
        statement.setString(8, entity.getResultString());
        statement.setDouble(9, entity.getResultValue());
        statement.setString(10, entity.getResultUnit());
        statement.setString(11, entity.getResultMeasurementType());
        if (entity.getErrorMessage() == null) {
            statement.setNull(12, Types.VARCHAR);
        } else {
            statement.setString(12, entity.getErrorMessage());
        }
        statement.setBoolean(13, entity.isError());
    }

    @Override
    public List<QuantityMeasurementEntity> getAllMeasurements() {
        return query(SELECT_ALL_SQL, null);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return query(SELECT_BY_OPERATION_SQL, operation);
    }

    @Override
    public List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        return query(SELECT_BY_TYPE_SQL, measurementType);
    }

    // runs a select with at most one parameter and maps every row to an entity
    private List<QuantityMeasurementEntity> query(String sql, String parameter) {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (parameter != null) {
                statement.setString(1, parameter);
            }
            List<QuantityMeasurementEntity> results = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    results.add(mapRow(rs));
                }
            }
            return results;
        } catch (SQLException e) {
            throw new DatabaseException("Could not read measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    private QuantityMeasurementEntity mapRow(ResultSet rs) throws SQLException {
        return new QuantityMeasurementEntity(
                rs.getDouble("this_value"), rs.getString("this_unit"), rs.getString("this_measurement_type"),
                rs.getDouble("that_value"), rs.getString("that_unit"), rs.getString("that_measurement_type"),
                rs.getString("operation"), rs.getString("result_string"), rs.getDouble("result_value"),
                rs.getString("result_unit"), rs.getString("result_measurement_type"),
                rs.getString("error_message"), rs.getBoolean("is_error"));
    }

    @Override
    public int getTotalCount() {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(COUNT_SQL);
             ResultSet rs = statement.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseException("Could not count measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public void deleteAll() {
        Connection connection = connectionPool.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SQL)) {
            int deleted = statement.executeUpdate();
            logger.info("Deleted {} measurement(s) from the database", deleted);
        } catch (SQLException e) {
            throw new DatabaseException("Could not delete measurements: " + e.getMessage(), e);
        } finally {
            connectionPool.releaseConnection(connection);
        }
    }

    @Override
    public String getPoolStatistics() {
        return connectionPool.getStatistics();
    }

    @Override
    public void releaseResources() {
        connectionPool.closeAll();
    }

    private void resetAutoCommit(Connection connection) {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            logger.warn("Could not reset auto commit: {}", e.getMessage());
        }
    }
}
