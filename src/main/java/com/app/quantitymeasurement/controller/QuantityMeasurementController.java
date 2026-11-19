package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.model.QuantityInputDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementDTO;
import com.app.quantitymeasurement.service.IQuantityMeasurementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing the quantity measurement operations. It only
 * maps HTTP requests to service calls, all logic lives in the service.
 * Responses are JSON by default and XML when the client asks for it.
 *
 * @author Navya Raj
 */
@RestController
@RequestMapping(value = "/api/v1/quantities", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
public class QuantityMeasurementController {

    private static final Logger logger = LoggerFactory.getLogger(QuantityMeasurementController.class);

    private final IQuantityMeasurementService quantityMeasurementService;

    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = quantityMeasurementService;
    }

    @PostMapping("/compare")
    @Operation(summary = "Compare two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performComparison(@Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        logger.info("POST /compare {}", quantityInputDTO);
        return ResponseEntity.ok(quantityMeasurementService.compare(
                quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO()));
    }

    @PostMapping("/convert")
    @Operation(summary = "Convert a quantity to the unit of the second quantity")
    public ResponseEntity<QuantityMeasurementDTO> performConversion(@Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        logger.info("POST /convert {}", quantityInputDTO);
        return ResponseEntity.ok(quantityMeasurementService.convert(
                quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO()));
    }

    @PostMapping("/add")
    @Operation(summary = "Add two quantities")
    public ResponseEntity<QuantityMeasurementDTO> performAddition(@Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        logger.info("POST /add {}", quantityInputDTO);
        QuantityMeasurementDTO result = quantityInputDTO.getTargetQuantityDTO() == null
                ? quantityMeasurementService.add(quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO())
                : quantityMeasurementService.add(quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO(),
                quantityInputDTO.getTargetQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/subtract")
    @Operation(summary = "Subtract the second quantity from the first")
    public ResponseEntity<QuantityMeasurementDTO> performSubtraction(@Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        logger.info("POST /subtract {}", quantityInputDTO);
        QuantityMeasurementDTO result = quantityInputDTO.getTargetQuantityDTO() == null
                ? quantityMeasurementService.subtract(quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO())
                : quantityMeasurementService.subtract(quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO(),
                quantityInputDTO.getTargetQuantityDTO());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/divide")
    @Operation(summary = "Divide the first quantity by the second")
    public ResponseEntity<QuantityMeasurementDTO> performDivision(@Valid @RequestBody QuantityInputDTO quantityInputDTO) {
        logger.info("POST /divide {}", quantityInputDTO);
        return ResponseEntity.ok(quantityMeasurementService.divide(
                quantityInputDTO.getThisQuantityDTO(), quantityInputDTO.getThatQuantityDTO()));
    }

    @GetMapping("/history/operation/{operation}")
    @Operation(summary = "Get the history of an operation, e.g. COMPARE or ADD")
    public ResponseEntity<List<QuantityMeasurementDTO>> getOperationHistory(@PathVariable String operation) {
        return ResponseEntity.ok(quantityMeasurementService.getOperationHistory(operation));
    }

    @GetMapping("/history/type/{measurementType}")
    @Operation(summary = "Get the history of a measurement type, e.g. LengthUnit")
    public ResponseEntity<List<QuantityMeasurementDTO>> getMeasurementsByType(@PathVariable String measurementType) {
        return ResponseEntity.ok(quantityMeasurementService.getMeasurementsByType(measurementType));
    }

    @GetMapping("/count/{operation}")
    @Operation(summary = "Count the successful operations of a type")
    public ResponseEntity<Long> getOperationCount(@PathVariable String operation) {
        return ResponseEntity.ok(quantityMeasurementService.getOperationCount(operation));
    }

    @GetMapping("/history/errored")
    @Operation(summary = "Get the history of failed operations")
    public ResponseEntity<List<QuantityMeasurementDTO>> getErrorHistory() {
        return ResponseEntity.ok(quantityMeasurementService.getErrorHistory());
    }
}
