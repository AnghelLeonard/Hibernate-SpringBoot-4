package com.bookstore;

import com.bookstore.metadata.ColumnMetadata;
import com.bookstore.metadata.ForeignKeyMetadata;
import com.bookstore.metadata.IndexMetadata;
import com.bookstore.metadata.TableMetadata;
import com.bookstore.service.JdbcDatabaseMetadataService;
import java.util.List;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final JdbcDatabaseMetadataService jdbcDatabaseMetadataService;

    public MainApplication(JdbcDatabaseMetadataService jdbcDatabaseMetadataService) {
        this.jdbcDatabaseMetadataService = jdbcDatabaseMetadataService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            List<TableMetadata> tables = jdbcDatabaseMetadataService.extractSchemaMetadata("public");

            System.out.println("\n======================================================================");
            System.out.println(" DATABASE SCHEMA METADATA (schema: public)");
            System.out.println("======================================================================");

            for (TableMetadata table : tables) {
                System.out.printf("%nTABLE: %s%n", table.tableName());
                System.out.printf("  Primary Keys : %s%n",
                    table.primaryKeys().isEmpty() ? "None" : String.join(", ", table.primaryKeys()));

                System.out.println("  Columns:");
                System.out.printf("    %-18s %-14s %-8s %-10s %s%n",
                    "NAME", "TYPE", "SIZE", "NULLABLE", "DEFAULT");
                System.out.println("    ------------------------------------------------------------------");

                for (ColumnMetadata col : table.columns()) {
                    System.out.printf("    %-18s %-14s %-8d %-10s %s%n",
                        col.name(),
                        col.dataType(),
                        col.columnSize(),
                        col.nullable() ? "YES" : "NO",
                        col.defaultValue() != null ? col.defaultValue() : "-"
                    );
                }

                if (!table.foreignKeys().isEmpty()) {
                    System.out.println("  Foreign Keys:");
                    for (ForeignKeyMetadata fk : table.foreignKeys()) {
                        System.out.printf("    - %s (%s -> %s.%s)%n",
                            fk.fkName(),
                            fk.fkColumn(),
                            fk.referencedTable(),
                            fk.referencedColumn()
                        );
                    }
                }

                if (!table.indexes().isEmpty()) {
                    System.out.println("  Indexes:");
                    for (IndexMetadata idx : table.indexes()) {
                        System.out.printf("    - %-24s | Unique: %-5s | Columns: (%s)%n",
                            idx.indexName(),
                            idx.unique() ? "YES" : "NO",
                            String.join(", ", idx.columns())
                        );
                    }
                }
            }

            System.out.println("\n======================================================================\n");
        };
    }
}
