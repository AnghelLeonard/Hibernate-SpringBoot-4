package com.bookstore.dto;

import java.time.Instant;
import java.util.List;

public class KeysetDto<T> {

    private final List<T> content;
    private final Long lastId;
    private final Instant lastCreatedAt;
    private final boolean hasNext;

    public KeysetDto(List<T> content, Long lastId, Instant lastCreatedAt, boolean hasNext) {
        this.content = content;
        this.lastId = lastId;
        this.lastCreatedAt = lastCreatedAt;
        this.hasNext = hasNext;
    }

    // Getters
    public List<T> getContent() {
        return content;
    }

    public Long getLastId() {
        return lastId;
    }

    public Instant getLastCreatedAt() {
        return lastCreatedAt;
    }

    public boolean isHasNext() {
        return hasNext;
    }
}
