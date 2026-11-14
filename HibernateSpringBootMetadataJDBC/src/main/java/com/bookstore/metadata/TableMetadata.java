package com.bookstore.metadata;

import java.util.List;

public record TableMetadata(
    String schema,
    String tableName,
    List<String> primaryKeys,
    List<ColumnMetadata> columns,
    List<ForeignKeyMetadata> foreignKeys,
    List<IndexMetadata> indexes
) {}
