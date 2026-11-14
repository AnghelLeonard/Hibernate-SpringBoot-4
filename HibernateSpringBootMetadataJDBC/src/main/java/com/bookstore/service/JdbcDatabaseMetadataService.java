package com.bookstore.service;

import com.bookstore.metadata.ColumnMetadata;
import com.bookstore.metadata.ForeignKeyMetadata;
import com.bookstore.metadata.IndexMetadata;
import com.bookstore.metadata.TableMetadata;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.stereotype.Service;

@Service
public class JdbcDatabaseMetadataService {

    private final DataSource dataSource;

    public JdbcDatabaseMetadataService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<TableMetadata> extractSchemaMetadata(String schemaPattern) throws SQLException {
        List<TableMetadata> tables = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();

            try (ResultSet rsTables = metaData.getTables(catalog, schemaPattern, "%", new String[]{"TABLE"})) {
                while (rsTables.next()) {
                    String schema = rsTables.getString("TABLE_SCHEM");
                    String tableName = rsTables.getString("TABLE_NAME");

                    List<String> primaryKeys = extractPrimaryKeys(metaData, catalog, schema, tableName);
                    List<ColumnMetadata> columns = extractColumns(metaData, catalog, schema, tableName);
                    List<ForeignKeyMetadata> foreignKeys = extractForeignKeys(metaData, catalog, schema, tableName);
                    List<IndexMetadata> indexes = extractIndexes(metaData, catalog, schema, tableName);

                    tables.add(new TableMetadata(schema, tableName, primaryKeys, columns, foreignKeys, indexes));
                }
            }
        }
        return tables;
    }

    private List<String> extractPrimaryKeys(
            DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        List<String> pks = new ArrayList<>();
        try (ResultSet rsPk = metaData.getPrimaryKeys(catalog, schema, tableName)) {
            while (rsPk.next()) {
                pks.add(rsPk.getString("COLUMN_NAME"));
            }
        }
        return pks;
    }

    private List<ColumnMetadata> extractColumns(
            DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        List<ColumnMetadata> columns = new ArrayList<>();
        try (ResultSet rsCols = metaData.getColumns(catalog, schema, tableName, "%")) {
            while (rsCols.next()) {
                columns.add(new ColumnMetadata(
                    rsCols.getString("COLUMN_NAME"),
                    rsCols.getString("TYPE_NAME"),
                    rsCols.getInt("COLUMN_SIZE"),
                    rsCols.getInt("NULLABLE") == DatabaseMetaData.columnNullable,
                    rsCols.getString("COLUMN_DEF")
                ));
            }
        }
        return columns;
    }

    private List<ForeignKeyMetadata> extractForeignKeys(
            DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        List<ForeignKeyMetadata> fks = new ArrayList<>();
        try (ResultSet rsFk = metaData.getImportedKeys(catalog, schema, tableName)) {
            while (rsFk.next()) {
                fks.add(new ForeignKeyMetadata(
                    rsFk.getString("FK_NAME"),
                    rsFk.getString("FKCOLUMN_NAME"),
                    rsFk.getString("PKTABLE_NAME"),
                    rsFk.getString("PKCOLUMN_NAME")
                ));
            }
        }
        return fks;
    }

    private List<IndexMetadata> extractIndexes(
            DatabaseMetaData metaData, String catalog, String schema, String tableName) throws SQLException {
        Map<String, IndexMetadata> indexMap = new LinkedHashMap<>();
        
        try (ResultSet rsIdx = metaData.getIndexInfo(catalog, schema, tableName, false, false)) {
            while (rsIdx.next()) {
                if (rsIdx.getShort("TYPE") == DatabaseMetaData.tableIndexStatistic) {
                    continue;
                }

                String indexName = rsIdx.getString("INDEX_NAME");
                String columnName = rsIdx.getString("COLUMN_NAME");
                boolean isUnique = !rsIdx.getBoolean("NON_UNIQUE");

                if (indexName != null && columnName != null) {
                    indexMap.computeIfAbsent(indexName,
                        name -> new IndexMetadata(name, isUnique, new ArrayList<>())
                    ).columns().add(columnName);
                }
            }
        }
        return new ArrayList<>(indexMap.values());
    }
}