package com.bookstore.exception;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(
            IllegalArgumentException ex, HttpServletRequest request) {
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Filter Expression",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ProblemDetail handleDateTimeParse(
            DateTimeParseException ex, HttpServletRequest request) {
        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Date or Timestamp Format",
                "Could not parse value '" + ex.getParsedString()
                + "'. Expected ISO-8601 format (e.g., 2026-11-21 or 2026-11-21T09:30:00Z).",
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    public ProblemDetail handleInvalidDataAccess(
            InvalidDataAccessApiUsageException ex, HttpServletRequest request) {
        String message = ex.getMostSpecificCause() != null
                ? ex.getMostSpecificCause().getMessage()
                : ex.getMessage();

        return buildProblemDetail(
                HttpStatus.BAD_REQUEST,
                "Invalid Query Attribute or Value",
                message,
                request.getRequestURI()
        );
    }

    private ProblemDetail buildProblemDetail(
            HttpStatus status, String title, String detail, String path) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setType(URI.create("https://bookstore.com"));
        problem.setInstance(URI.create(path));
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
