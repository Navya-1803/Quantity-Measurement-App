package com.app.quantitymeasurement.util;

import com.app.quantitymeasurement.exception.DatabaseException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionPoolTest {

    private ConnectionPool pool;

    @BeforeEach
    void setUp() {
        pool = new ConnectionPool("org.h2.Driver", "jdbc:h2:mem:pooltest;DB_CLOSE_DELAY=-1", "sa", "", 2, 200);
    }

    @AfterEach
    void tearDown() {
        pool.closeAll();
    }

    @Test
    void testConnectionPool_Initialization() {
        assertEquals(2, pool.getPoolSize());
        assertEquals(2, pool.getAvailableConnections());
        assertEquals(0, pool.getActiveConnections());
    }

    @Test
    void testConnectionPool_Acquire_Release() {
        Connection connection = pool.getConnection();
        assertNotNull(connection);
        assertEquals(1, pool.getActiveConnections());
        pool.releaseConnection(connection);
        assertEquals(0, pool.getActiveConnections());
    }

    @Test
    void testConnectionPool_AllConnectionsExhausted() {
        pool.getConnection();
        pool.getConnection();
        assertThrows(DatabaseException.class, () -> pool.getConnection());
    }

    @Test
    void testConnectionPool_Statistics() {
        pool.getConnection();
        assertEquals("Pool[total=2, active=1, idle=1]", pool.getStatistics());
    }

    @Test
    void testResourceCleanup_ConnectionClosed() throws Exception {
        Connection connection = pool.getConnection();
        pool.closeAll();
        assertTrue(connection.isClosed());
    }

    @Test
    void testInvalidConfiguration_Rejected() {
        assertThrows(IllegalArgumentException.class, () -> new ConnectionPool("org.h2.Driver", "", "sa", "", 2, 100));
        assertThrows(IllegalArgumentException.class, () -> new ConnectionPool("org.h2.Driver", "jdbc:h2:mem:x", "sa", "", 0, 100));
        assertThrows(DatabaseException.class, () -> new ConnectionPool("no.such.Driver", "jdbc:h2:mem:x", "sa", "", 1, 100));
    }

    @Test
    void testFromConfig() {
        ConnectionPool configured = ConnectionPool.fromConfig(new ApplicationConfig("application-test.properties"));
        assertEquals(3, configured.getPoolSize());
        configured.closeAll();
    }
}
