package com.bookstore.dto;

public record RecordAuthorSummary(
    Long id,
    Integer age,
    String name,
    String genre,
    String address,
    Integer rank,
    String nickname,
    String language
) {}