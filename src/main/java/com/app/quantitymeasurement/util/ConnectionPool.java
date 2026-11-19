package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * ConnectionPool keeps a fixed number of open database connections and
 * hands them out on request. Reusing connections is much faster than
 * opening a new one for every query.
 *
 * @author Navya Raj
 */
public class ConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);

    private final BlockingQueue<Connection> availableConnections;
    private final List<Connection> allConnections = new ArrayList<>();
    private final int poolSize;
    private final int timeoutMillis;

    public ConnectionPool(String driver, String url, String username, String password, int poolSize, int timeoutMillis) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Database url cannot be empty");
        }
        if (poolSize <= 0) {
            throw new IllegalArgumentException("Pool size must be greater than zero");
        }
        this.poolSize = poolSize;
        this.timeoutMillis = timeoutMillis;
        this.availableConnections = new ArrayBlockingQueue<>(poolSize);
        try {
            if (driver != null && !driver.isBlank()) {
                Class.forName(driver);
            }
            for (int i = 0; i < poolSize; i++) {
                Connection connection = DriverManager.getConnection(url, username, password);
                allConnections.add(connection);
                availableConnections.add(connection);
            }
        } catch (ClassNotFoundException | SQLException e) {
            closeAll();
            throw new DatabaseException("Could not initialize connection pool: " + e.getMessage(), e);
        }
        logger.info("Connection pool initialized with {} connections", poolSize);
    }

    /**
     * Creates a pool using the database settings of the application config.
     */
    public static ConnectionPool fromConfig(ApplicationConfig config) {
        return new ConnectionPool(config.getDbDriver(), config.getDbUrl(), config.getDbUsername(),
                config.getDbPassword(), config.getPoolSize(), config.getPoolTimeoutMillis());
    }

    /**
     * Takes a free connection, waiting up to the configured timeout.
     *
     * @throws DatabaseException if no connection becomes free in time
     */
    public Connection getConnection() {
        try {
            Connection connection = availableConnections.poll(timeoutMillis, TimeUnit.MILLISECONDS);
            if (connection == null) {
                throw new DatabaseException("All " + poolSize + " connections are in use, timed out after " + timeoutMillis + " ms");
            }
            return connection;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DatabaseException("Interrupted while waiting for a connection", e);
        }
    }

    /**
     * Gives a connection back to the pool so it can be reused.
     */
    public void releaseConnection(Connection connection) {
        if (connection != null && allConnections.contains(connection)) {
            availableConnections.offer(connection);
        }
    }

    public int getAvailableConnections() {
        return availableConnections.size();
    }

    public int getActiveConnections() {
        return poolSize - availableConnections.size();
    }

    public int getPoolSize() {
        return poolSize;
    }

    public String getStatistics() {
        return "Pool[total=" + poolSize + ", active=" + getActiveConnections() + ", idle=" + getAvailableConnections() + "]";
    }

    /**
     * Closes every connection of the pool.
     */
    public void closeAll() {
        for (Connection connection : allConnections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.warn("Could not close connection: {}", e.getMessage());
            }
        }
        allConnections.clear();
        availableConnections.clear();
        logger.info("Connection pool closed");
    }
}
