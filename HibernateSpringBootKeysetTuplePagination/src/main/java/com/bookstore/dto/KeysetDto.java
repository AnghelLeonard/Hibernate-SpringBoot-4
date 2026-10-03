package com.bookstore.dto;

import java.time.Instant;
import java.util.List;

public record KeysetDto<T> (List<T> content, 
        Instant lastCreatedAt, Long lastId, boolean hasNext) {}
