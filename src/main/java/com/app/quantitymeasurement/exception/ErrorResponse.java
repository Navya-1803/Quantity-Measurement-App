package com.app.quantitymeasurement.exception;

import java.time.LocalDateTime;

/**
 * Body returned by the API whenever a request fails.
 *
 * @author Navya Raj
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {

    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(LocalDateTime.now(), status, error, message, path);
    }
}
