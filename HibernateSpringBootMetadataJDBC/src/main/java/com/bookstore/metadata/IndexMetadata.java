package com.bookstore.metadata;

import java.util.List;

public record IndexMetadata(
    String indexName,
    boolean unique,
    List<String> columns
) {}
