package com.bookstore.dto;

import java.util.List;

public record KeysetDto<T>(List<T> content, String nextCursor, boolean hasNext) {}
