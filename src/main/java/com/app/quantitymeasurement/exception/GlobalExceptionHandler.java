package com.app.quantitymeasurement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler turns exceptions thrown by any controller into a
 * consistent JSON error response with the right HTTP status.
 *
 * @author Navya Raj
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String QUANTITY_ERROR = "Quantity Measurement Error";

    // invalid request body, e.g. a unit that does not belong to its measurement type
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Invalid request";
        logger.warn("Validation failed on {}: {}", request.getRequestURI(), message);
        return build(HttpStatus.BAD_REQUEST, QUANTITY_ERROR, message, request);
    }

    // business rule violations reported by the service
    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<ErrorResponse> handleQuantityException(QuantityMeasurementException ex, HttpServletRequest request) {
        logger.warn("Quantity measurement error on {}: {}", request.getRequestURI(), ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, QUANTITY_ERROR, ex.getMessage(), request);
    }

    // unknown operation names in path variables
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, QUANTITY_ERROR, ex.getMessage(), request);
    }

    // body missing or not valid JSON
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableBody(HttpMessageNotReadableException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "Malformed Request", "Request body is missing or not valid JSON", request);
    }

    // anything unexpected, for example division by zero
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        logger.error("Unexpected error on {}: {}", request.getRequestURI(), ex.getMessage());
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message, HttpServletRequest request) {
        return ResponseEntity.status(status).body(ErrorResponse.of(status.value(), error, message, request.getRequestURI()));
    }
}
