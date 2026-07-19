package com.bookstore.forum.config;

import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import oracle.jdbc.pool.OracleDataSource;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.tool.schema.extract.internal.SequenceInformationExtractorNoOpImpl;
import org.hibernate.tool.schema.extract.spi.SequenceInformationExtractor;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.OracleContainer;

import javax.sql.DataSource;

/**
 * Resolves the test {@link DataSource} using the local Oracle XE instance
 * first and, only if that's not reachable, falls back to a reusable
 * Testcontainers instance. On Oracle, the schema is the {@code oracle} user
 * itself, so there is no separate {@code bookstoredb} database to create.
 */
public class OracleDataSourceProvider extends AbstractContainerDataSourceProvider {

    private static final String LOCAL_JDBC_URL = "jdbc:oracle:thin:@localhost:1521/xe";

    public static final OracleDataSourceProvider INSTANCE = new OracleDataSourceProvider();

    @Override
    public Database database() {
        return Database.ORACLE;
    }

    @Override
    public String hibernateDialect() {
        return FastOracleDialect.class.getName();
    }

    @Override
    protected String defaultJdbcUrl() {
        return LOCAL_JDBC_URL;
    }

    @Override
    protected DataSource newDataSource() {
        try {
            OracleDataSource dataSource = new OracleDataSource();
            JdbcDatabaseContainer container = getContainer();
            if (container != null) {
                dataSource.setDatabaseName(container.getDatabaseName());
            }
            dataSource.setURL(url());
            dataSource.setUser(username());
            dataSource.setPassword(password());
            return dataSource;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String username() {
        return "oracle";
    }

    @Override
    public String password() {
        return "admin";
    }

    @Override
    public JdbcDatabaseContainer newJdbcDatabaseContainer() {
        return new OracleContainer("gvenzl/oracle-xe:21.3.0-slim");
    }

    @Override
    public boolean supportsDatabaseName() {
        return false;
    }

    /**
     * Skips the sequence metadata extraction, which is very slow on Oracle XE.
     */
    public static class FastOracleDialect extends OracleDialect {
        @Override
        public SequenceInformationExtractor getSequenceInformationExtractor() {
            return SequenceInformationExtractorNoOpImpl.INSTANCE;
        }
    }
}
