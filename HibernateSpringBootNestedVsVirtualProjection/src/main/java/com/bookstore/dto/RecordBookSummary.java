package com.bookstore.dto;

public record RecordBookSummary(
    String title,
    Integer rank,
    RecordAuthorSummary author
) {
    public record RecordAuthorSummary(String name, String genre) {}
        
    public RecordBookSummary(String title, Integer rank, String name, String genre) {
        this(title, rank, new RecordAuthorSummary(name, genre));
    }        
}
