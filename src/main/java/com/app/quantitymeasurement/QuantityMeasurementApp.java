package com.app.quantitymeasurement;

import com.app.quantitymeasurement.QuantityDTO.LengthUnit;
import com.app.quantitymeasurement.QuantityDTO.TemperatureUnit;
import com.app.quantitymeasurement.QuantityDTO.VolumeUnit;
import com.app.quantitymeasurement.QuantityDTO.WeightUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementApp is the entry point of the application. It creates
 * the repository, service and controller (Factory), and runs every
 * operation through the controller (Facade). The app itself is a Singleton.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementApp {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApp.class);

    private static QuantityMeasurementApp instance;

    private final IQuantityMeasurementRepository repository;
    private final QuantityMeasurementController controller;

    private QuantityMeasurementApp() {
        this.repository = createRepository();
        this.controller = createController(createService(repository));
        logger.info("QuantityMeasurementApp initialized");
    }

    public static synchronized QuantityMeasurementApp getInstance() {
        if (instance == null) {
            instance = new QuantityMeasurementApp();
        }
        return instance;
    }

    // factory methods for each layer
    private static IQuantityMeasurementRepository createRepository() {
        return QuantityMeasurementCacheRepository.getInstance();
    }

    private static IQuantityMeasurementService createService(IQuantityMeasurementRepository repository) {
        return new QuantityMeasurementServiceImpl(repository);
    }

    private static QuantityMeasurementController createController(IQuantityMeasurementService service) {
        return new QuantityMeasurementController(service);
    }

    public QuantityMeasurementController getController() {
        return controller;
    }

    public IQuantityMeasurementRepository getRepository() {
        return repository;
    }

    public static void main(String[] args) {
        QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();
        QuantityMeasurementController controller = app.getController();

        // length
        controller.performComparison(new QuantityDTO(1.0, LengthUnit.FEET), new QuantityDTO(12.0, LengthUnit.INCHES));
        controller.performConversion(new QuantityDTO(1.0, LengthUnit.FEET), new QuantityDTO(0.0, LengthUnit.INCHES));
        controller.performAddition(new QuantityDTO(1.0, LengthUnit.FEET), new QuantityDTO(12.0, LengthUnit.INCHES));
        controller.performSubtraction(new QuantityDTO(10.0, LengthUnit.FEET), new QuantityDTO(6.0, LengthUnit.INCHES),
                new QuantityDTO(0.0, LengthUnit.INCHES));
        controller.performDivision(new QuantityDTO(24.0, LengthUnit.INCHES), new QuantityDTO(2.0, LengthUnit.FEET));

        // weight
        controller.performComparison(new QuantityDTO(1.0, WeightUnit.KILOGRAM), new QuantityDTO(1000.0, WeightUnit.GRAM));
        controller.performAddition(new QuantityDTO(10.0, WeightUnit.KILOGRAM), new QuantityDTO(5000.0, WeightUnit.GRAM),
                new QuantityDTO(0.0, WeightUnit.GRAM));

        // volume
        controller.performConversion(new QuantityDTO(1.0, VolumeUnit.GALLON), new QuantityDTO(0.0, VolumeUnit.LITRE));
        controller.performSubtraction(new QuantityDTO(5.0, VolumeUnit.LITRE), new QuantityDTO(500.0, VolumeUnit.MILLILITRE));

        // temperature: comparison and conversion work, arithmetic is rejected
        controller.performComparison(new QuantityDTO(0.0, TemperatureUnit.CELSIUS), new QuantityDTO(32.0, TemperatureUnit.FAHRENHEIT));
        controller.performConversion(new QuantityDTO(100.0, TemperatureUnit.CELSIUS), new QuantityDTO(0.0, TemperatureUnit.FAHRENHEIT));
        controller.performAddition(new QuantityDTO(100.0, TemperatureUnit.CELSIUS), new QuantityDTO(50.0, TemperatureUnit.CELSIUS));

        // error cases
        controller.performAddition(new QuantityDTO(1.0, LengthUnit.FEET), new QuantityDTO(1.0, WeightUnit.KILOGRAM));
        controller.performDivision(new QuantityDTO(10.0, LengthUnit.FEET), new QuantityDTO(0.0, LengthUnit.FEET));

        logger.info("Total measurements stored: {}", app.getRepository().getAllMeasurements().size());
    }
}
