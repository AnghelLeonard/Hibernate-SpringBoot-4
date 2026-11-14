package com.bookstore.metadata;

public record ForeignKeyMetadata(
    String fkName,
    String fkColumn,
    String referencedTable,
    String referencedColumn
) {}
