package com.bookstore.dto;

public record SimpleRecordBookSummary(
        String title,
        Integer rank,
        SimpleRecordAuthorSummary author
        ) {

    public record SimpleRecordAuthorSummary(String name, String genre) {}
}
