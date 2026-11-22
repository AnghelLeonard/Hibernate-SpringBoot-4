package com.bookstore.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<Map<String, String>> handleOutOfStock(OutOfStockException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", ex.getMessage())); // HTTP 409
    }

    @ExceptionHandler(IdempotencyPayloadMismatchException.class)
    public ResponseEntity<Map<String, String>> handlePayloadMismatch(IdempotencyPayloadMismatchException ex) {
        return ResponseEntity.unprocessableEntity()
                .body(Map.of("error", ex.getMessage())); // HTTP 422
    }

    @ExceptionHandler(HighContentionException.class)
    public ResponseEntity<Map<String, String>> handleHighContention(HighContentionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "1")
                .body(Map.of("error", ex.getMessage())); // HTTP 503
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage())); // HTTP 400
    }
}
