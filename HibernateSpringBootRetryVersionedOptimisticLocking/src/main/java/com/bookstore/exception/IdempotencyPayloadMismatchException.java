package com.bookstore.exception;

public class IdempotencyPayloadMismatchException extends RuntimeException {

    public IdempotencyPayloadMismatchException(String message) {
        super(message);
    }
}
