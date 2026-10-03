package com.bookstore.dto;

import java.util.List;

public record PagedResponse<T>(
    List<T> content,
    String continuationToken,
    boolean hasMore
) {}
