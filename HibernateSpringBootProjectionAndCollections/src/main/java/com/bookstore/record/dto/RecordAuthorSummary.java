package com.bookstore.record.dto;

import java.util.List;

public record RecordAuthorSummary(String name, String genre, List<RecordBookSummary> books) {
    public record RecordBookSummary(String title) {}
}
