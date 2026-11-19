package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.entity.QuantityDTO;
import com.app.quantitymeasurement.exception.QuantityMeasurementException;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * QuantityMeasurementController is the facade used by the application to
 * run operations. It only passes requests to the service and shows the
 * results, it contains no business logic.
 *
 * @author Navya Raj
 */
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    private final IQuantityMeasurementService quantityMeasurementService;

    // the service is injected through the constructor
    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        if (quantityMeasurementService == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }
        this.quantityMeasurementService = quantityMeasurementService;
        logger.info("QuantityMeasurementController initialized");
    }

    public Boolean performComparison(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            boolean result = quantityMeasurementService.compare(thisQuantityDTO, thatQuantityDTO);
            displayResult("Comparison", thisQuantityDTO + " == " + thatQuantityDTO, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Comparison", e);
            return null;
        }
    }

    public QuantityDTO performConversion(QuantityDTO thisQuantityDTO, QuantityDTO targetUnitDTO) {
        try {
            QuantityDTO result = quantityMeasurementService.convert(thisQuantityDTO, targetUnitDTO);
            displayResult("Conversion", thisQuantityDTO + " to " + targetUnitDTO.getUnit(), result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Conversion", e);
            return null;
        }
    }

    public QuantityDTO performAddition(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            QuantityDTO result = quantityMeasurementService.add(thisQuantityDTO, thatQuantityDTO);
            displayResult("Addition", thisQuantityDTO + " + " + thatQuantityDTO, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Addition", e);
            return null;
        }
    }

    public QuantityDTO performAddition(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        try {
            QuantityDTO result = quantityMeasurementService.add(thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
            displayResult("Addition", thisQuantityDTO + " + " + thatQuantityDTO + " in " + targetUnitDTO.getUnit(), result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Addition", e);
            return null;
        }
    }

    public QuantityDTO performSubtraction(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            QuantityDTO result = quantityMeasurementService.subtract(thisQuantityDTO, thatQuantityDTO);
            displayResult("Subtraction", thisQuantityDTO + " - " + thatQuantityDTO, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Subtraction", e);
            return null;
        }
    }

    public QuantityDTO performSubtraction(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO, QuantityDTO targetUnitDTO) {
        try {
            QuantityDTO result = quantityMeasurementService.subtract(thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
            displayResult("Subtraction", thisQuantityDTO + " - " + thatQuantityDTO + " in " + targetUnitDTO.getUnit(), result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Subtraction", e);
            return null;
        }
    }

    public Double performDivision(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        try {
            double result = quantityMeasurementService.divide(thisQuantityDTO, thatQuantityDTO);
            displayResult("Division", thisQuantityDTO + " / " + thatQuantityDTO, result);
            return result;
        } catch (QuantityMeasurementException e) {
            displayError("Division", e);
            return null;
        }
    }

    /**
     * Formats a successful result for the console.
     */
    public String displayResult(String operation, String input, Object result) {
        String message = operation + " -> Input: " + input + " | Output: " + result;
        logger.info(message);
        return message;
    }

    /**
     * Formats a failed operation for the console.
     */
    public String displayError(String operation, Exception exception) {
        String message = operation + " -> Error: " + exception.getMessage();
        logger.error(message);
        return message;
    }
}
