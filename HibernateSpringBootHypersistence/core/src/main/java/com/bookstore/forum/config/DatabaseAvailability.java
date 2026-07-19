package com.bookstore.forum.config;

import java.sql.Connection;
import java.util.EnumMap;
import java.util.Map;

import javax.sql.DataSource;

/**
 * Probes whether a {@link DatabaseType} can be reached, either through the
 * local server or through the Testcontainers fallback. The result is cached
 * per database so the (potentially container-starting) probe runs once.
 */
public final class DatabaseAvailability {

    private static final Map<DatabaseType, Boolean> CACHE = new EnumMap<>(DatabaseType.class);

    private DatabaseAvailability() {
    }

    public static synchronized boolean isAvailable(DatabaseType databaseType) {
        return CACHE.computeIfAbsent(databaseType, DatabaseAvailability::probe);
    }

    private static boolean probe(DatabaseType databaseType) {
        try {
            DataSource dataSource = databaseType.provider().dataSource();
            try (Connection connection = dataSource.getConnection()) {
                return connection.isValid(5);
            }
        } catch (Throwable failure) {
            return false;
        }
    }
}
