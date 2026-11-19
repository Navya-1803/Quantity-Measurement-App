package com.app.quantitymeasurement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot entry point of the Quantity Measurement application. Starts
 * the embedded server that serves the REST API on /api/v1/quantities.
 *
 * @author Navya Raj
 */
@SpringBootApplication
public class QuantityMeasurementApplication {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(QuantityMeasurementApplication.class, args);
        logger.info("Quantity Measurement Application started");
    }
}
