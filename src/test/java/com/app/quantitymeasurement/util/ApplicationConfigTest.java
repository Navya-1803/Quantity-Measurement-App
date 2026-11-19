package com.app.quantitymeasurement.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationConfigTest {

    @AfterEach
    void clearOverride() {
        System.clearProperty("repository.type");
    }

    @Test
    void testDatabaseConfiguration_LoadedFromProperties() {
        ApplicationConfig config = new ApplicationConfig("application-test.properties");
        assertEquals("org.h2.Driver", config.getDbDriver());
        assertTrue(config.getDbUrl().startsWith("jdbc:h2:mem:"));
        assertEquals("sa", config.getDbUsername());
        assertEquals(3, config.getPoolSize());
        assertEquals(500, config.getPoolTimeoutMillis());
    }

    @Test
    void testDefaultConfiguration_Loaded() {
        ApplicationConfig config = new ApplicationConfig();
        assertEquals("database", config.getRepositoryType());
        assertEquals("db/schema.sql", config.getDbSchema());
    }

    @Test
    void testPropertiesConfiguration_EnvironmentOverride() {
        System.setProperty("repository.type", "cache");
        assertEquals("cache", new ApplicationConfig("application-test.properties").getRepositoryType());
    }

    @Test
    void testMissingConfigurationFile_Throws() {
        assertThrows(IllegalStateException.class, () -> new ApplicationConfig("missing.properties"));
    }

    @Test
    void testInvalidNumber_UsesDefault() {
        ApplicationConfig config = new ApplicationConfig("application-test.properties");
        assertEquals(7, config.getIntProperty("db.username", 7));
        assertEquals("fallback", config.getProperty("not.defined", "fallback"));
    }
}
