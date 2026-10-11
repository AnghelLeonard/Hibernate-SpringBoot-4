package com.bookstore.dto;

import java.util.List;

public record KeysetDto<T>(List<T> content, Long nextAuthorId, Long nextBookId, boolean hasMore) {}
