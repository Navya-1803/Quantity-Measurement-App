package com.app.quantitymeasurement.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ApplicationConfig loads the application settings from a properties file
 * on the classpath. Any setting can be overridden with a JVM system
 * property of the same name (for example -Drepository.type=cache), which
 * makes it easy to switch environments without changing the file.
 *
 * @author Navya Raj
 */
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static final String DEFAULT_CONFIG_FILE = "application.properties";

    private final Properties properties = new Properties();

    public ApplicationConfig() {
        this(DEFAULT_CONFIG_FILE);
    }

    public ApplicationConfig(String configFile) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (in == null) {
                throw new IllegalStateException("Configuration file not found: " + configFile);
            }
            properties.load(in);
            logger.info("Loaded configuration from {}", configFile);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read configuration file: " + configFile, e);
        }
    }

    /**
     * @return value of the key, a system property with the same name wins over the file
     */
    public String getProperty(String key) {
        return System.getProperty(key, properties.getProperty(key));
    }

    public String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value == null ? defaultValue : value;
    }

    public int getIntProperty(String key, int defaultValue) {
        String value = getProperty(key);
        try {
            return value == null ? defaultValue : Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            logger.warn("Invalid number for {}: {}, using default {}", key, value, defaultValue);
            return defaultValue;
        }
    }

    public String getRepositoryType() {
        return getProperty("repository.type", "cache");
    }

    public String getDbDriver() {
        return getProperty("db.driver");
    }

    public String getDbUrl() {
        return getProperty("db.url");
    }

    public String getDbUsername() {
        return getProperty("db.username", "");
    }

    public String getDbPassword() {
        return getProperty("db.password", "");
    }

    public String getDbSchema() {
        return getProperty("db.schema", "db/schema.sql");
    }

    public int getPoolSize() {
        return getIntProperty("db.pool.size", 5);
    }

    public int getPoolTimeoutMillis() {
        return getIntProperty("db.pool.timeout.ms", 3000);
    }
}
