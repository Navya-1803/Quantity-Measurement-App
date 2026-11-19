package com.app.quantitymeasurement.repository;

import com.app.quantitymeasurement.entity.QuantityMeasurementEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * QuantityMeasurementCacheRepository keeps measurement records in an
 * in-memory list and also appends every record to a file, so the history
 * survives application restarts. It is a Singleton, there is only one
 * cache in the whole application.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementCacheRepository implements IQuantityMeasurementRepository {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementCacheRepository.class);

    public static final String DEFAULT_FILE_NAME = "quantity_measurement_repo.ser";

    private static QuantityMeasurementCacheRepository instance;

    private final List<QuantityMeasurementEntity> quantityCache = new ArrayList<>();
    private final File repositoryFile;

    // package-private so that tests can use a temporary file
    QuantityMeasurementCacheRepository(String fileName) {
        this.repositoryFile = new File(fileName);
        loadFromDisk();
        logger.info("Cache repository initialized with {} saved measurement(s)", quantityCache.size());
    }

    /**
     * @return the single shared instance of the cache repository
     */
    public static synchronized QuantityMeasurementCacheRepository getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementCacheRepository(DEFAULT_FILE_NAME);
        }
        return instance;
    }

    @Override
    public synchronized void save(QuantityMeasurementEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity to save cannot be null");
        }
        quantityCache.add(entity);
        saveToDisk(entity);
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getAllMeasurements() {
        return Collections.unmodifiableList(new ArrayList<>(quantityCache));
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation) {
        return quantityCache.stream()
                .filter(entity -> entity.getOperation() != null && entity.getOperation().equalsIgnoreCase(operation))
                .toList();
    }

    @Override
    public synchronized List<QuantityMeasurementEntity> getMeasurementsByMeasurementType(String measurementType) {
        return quantityCache.stream()
                .filter(entity -> measurementType != null && measurementType.equals(entity.getThisMeasurementType()))
                .toList();
    }

    @Override
    public synchronized int getTotalCount() {
        return quantityCache.size();
    }

    @Override
    public synchronized void deleteAll() {
        quantityCache.clear();
        // remove the saved file as well so deleted records do not come back
        if (repositoryFile.exists() && !repositoryFile.delete()) {
            logger.warn("Could not delete repository file {}", repositoryFile.getName());
        }
        logger.info("All cached measurements deleted");
    }

    // appends one entity to the file
    private void saveToDisk(QuantityMeasurementEntity entity) {
        boolean append = repositoryFile.exists() && repositoryFile.length() > 0;
        try (FileOutputStream fileOut = new FileOutputStream(repositoryFile, true);
             ObjectOutputStream out = append ? new AppendableObjectOutputStream(fileOut) : new ObjectOutputStream(fileOut)) {
            out.writeObject(entity);
        } catch (IOException e) {
            logger.error("Could not save measurement to disk: {}", e.getMessage());
        }
    }

    // reads all saved entities from the file into the cache
    private void loadFromDisk() {
        if (!repositoryFile.exists() || repositoryFile.length() == 0) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(repositoryFile))) {
            while (true) {
                quantityCache.add((QuantityMeasurementEntity) in.readObject());
            }
        } catch (java.io.EOFException e) {
            // end of file reached, everything is loaded
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Could not load measurements from disk: {}", e.getMessage());
        }
    }

    /**
     * ObjectOutputStream writes a header every time it is opened. When we
     * append to an existing file the header must be skipped, otherwise the
     * file cannot be read back.
     */
    private static class AppendableObjectOutputStream extends ObjectOutputStream {

        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            // header already written when the file was created
            reset();
        }
    }
}
