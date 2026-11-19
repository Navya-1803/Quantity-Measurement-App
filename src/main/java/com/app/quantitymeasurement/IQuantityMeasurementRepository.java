package com.app.quantitymeasurement;

import java.util.List;

/**
 * Contract for storing and reading quantity measurement records. Keeping it
 * as an interface allows switching between a cache and a database later
 * without changing the service layer.
 *
 * @author Navya Raj
 */
public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> getAllMeasurements();
}
