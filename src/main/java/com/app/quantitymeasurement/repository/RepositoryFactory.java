package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.util.ApplicationConfig;
import com.app.quantitymeasurement.util.ConnectionPool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RepositoryFactory creates the repository implementation selected in the
 * configuration (Factory pattern), so the rest of the app never needs to
 * know which storage is used.
 *
 * @author Navya Raj
 */
public final class RepositoryFactory {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryFactory.class);

    public static final String CACHE = "cache";
    public static final String DATABASE = "database";

    private RepositoryFactory() {
        // utility class, no objects needed
    }

    public static IQuantityMeasurementRepository createRepository(ApplicationConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Configuration cannot be null");
        }
        String type = config.getRepositoryType().trim().toLowerCase();
        logger.info("Creating '{}' repository", type);
        return switch (type) {
            case CACHE -> QuantityMeasurementCacheRepository.getInstance();
            case DATABASE -> new QuantityMeasurementDatabaseRepository(ConnectionPool.fromConfig(config), config.getDbSchema());
            default -> throw new IllegalArgumentException("Unknown repository type: " + type);
        };
    }
}
