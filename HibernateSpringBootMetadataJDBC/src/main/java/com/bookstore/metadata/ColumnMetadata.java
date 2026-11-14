package com.bookstore.metadata;

public record ColumnMetadata(
    String name,
    String dataType,
    int columnSize,
    boolean nullable,
    String defaultValue
) {}
