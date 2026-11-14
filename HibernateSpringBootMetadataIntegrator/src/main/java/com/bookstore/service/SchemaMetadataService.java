package com.bookstore.service;

import com.bookstore.config.DatabaseTableMetadataExtractor;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.hibernate.boot.model.relational.Database;
import org.springframework.stereotype.Service;

@Service
public class SchemaMetadataService {

    private final DatabaseTableMetadataExtractor extractor;

    public SchemaMetadataService(DatabaseTableMetadataExtractor extractor) {
        this.extractor = extractor;
    }

    public void printMappedTables() {
        Metadata metadata = extractor.getMetadata();
        Database database = extractor.getDatabase();

        for (Namespace namespace : database.getNamespaces()) {
            for (Table table : namespace.getTables()) {
                System.out.println("Table: " + table.getName());

                if (table.hasPrimaryKey()) {
                    System.out.println("  Primary Key: " + table.getPrimaryKey().getColumns());
                }

                for (Column column : table.getColumns()) {
                    System.out.printf("  Column: %-15s | SQL Type: %-15s | Nullable: %s%n",
                        column.getName(),
                        column.getSqlType(metadata),
                        column.isNullable()
                    );
                }

                for (ForeignKey fk : table.getForeignKeyCollection()) {
                    System.out.printf("  FK: %s -> %s%n",
                        fk.getName(),
                        fk.getReferencedTable().getName()
                    );
                }
            }
        }
    }
}