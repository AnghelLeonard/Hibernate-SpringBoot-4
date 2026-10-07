package com.bookstore.dto;

import java.util.List;

public record WindowDto<T>(List<T> content, String continuationToken, boolean hasMore) {}
