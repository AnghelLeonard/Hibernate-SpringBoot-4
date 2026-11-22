package com.bookstore.exception;

public class HighContentionException extends RuntimeException {

    public HighContentionException(String message, Throwable cause) {
        super(message, cause);
    }
}
