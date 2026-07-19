package com.bookstore.forum.config;

import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;

import java.util.function.Supplier;

/**
 * The databases the example modules can run against. Each constant exposes the
 * matching {@link AbstractContainerDataSourceProvider}, which resolves the
 * local server first and falls back to a reusable Testcontainers instance.
 */
public enum DatabaseType {

    MYSQL(() -> MySQLDataSourceProvider.INSTANCE),
    POSTGRESQL(() -> PostgreSQLDataSourceProvider.INSTANCE),
    SQLSERVER(() -> SQLServerDataSourceProvider.INSTANCE),
    ORACLE(() -> OracleDataSourceProvider.INSTANCE);

    private final Supplier<AbstractContainerDataSourceProvider> providerSupplier;

    DatabaseType(Supplier<AbstractContainerDataSourceProvider> providerSupplier) {
        this.providerSupplier = providerSupplier;
    }

    public AbstractContainerDataSourceProvider provider() {
        return providerSupplier.get();
    }
}
