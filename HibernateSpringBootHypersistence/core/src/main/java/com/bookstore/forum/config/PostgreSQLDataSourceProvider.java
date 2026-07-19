package com.bookstore.forum.config;

import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.PostgreSQLDialect;
import org.postgresql.ds.PGSimpleDataSource;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Resolves the test {@link DataSource} using the local PostgreSQL server first
 * and, only if that's not reachable, falls back to a reusable Testcontainers
 * instance. Since PostgreSQL cannot create a database on connect, the missing
 * {@code bookstoredb} database is created through the {@code postgres}
 * maintenance database on first use.
 */
public class PostgreSQLDataSourceProvider extends AbstractContainerDataSourceProvider {

    private static final String LOCAL_JDBC_URL =
        "jdbc:postgresql://localhost:5432/bookstoredb";

    private static final String MAINTENANCE_JDBC_URL =
        "jdbc:postgresql://localhost:5432/postgres";

    private static final String[][] LOCAL_CREDENTIALS = {
        {"postgres", "root"},
        {"postgres", "admin"}
    };

    public static final PostgreSQLDataSourceProvider INSTANCE = new PostgreSQLDataSourceProvider();

    private final String username;
    private final String password;

    private PostgreSQLDataSourceProvider() {
        String[] credentials = resolveLocalCredentials();
        this.username = credentials[0];
        this.password = credentials[1];
    }

    private static String[] resolveLocalCredentials() {
        for (String[] credentials : LOCAL_CREDENTIALS) {
            if (canConnect(LOCAL_JDBC_URL, credentials)) {
                return credentials;
            }
        }
        for (String[] credentials : LOCAL_CREDENTIALS) {
            try (Connection connection = DriverManager.getConnection(
                    MAINTENANCE_JDBC_URL, credentials[0], credentials[1]);
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("create database bookstoredb");
            } catch (SQLException ignore) {
            }
            if (canConnect(LOCAL_JDBC_URL, credentials)) {
                return credentials;
            }
        }
        return LOCAL_CREDENTIALS[0];
    }

    private static boolean canConnect(String url, String[] credentials) {
        try (Connection connection = DriverManager.getConnection(
                url, credentials[0], credentials[1])) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Database database() {
        return Database.POSTGRESQL;
    }

    @Override
    public String hibernateDialect() {
        return PostgreSQLDialect.class.getName();
    }

    @Override
    protected String defaultJdbcUrl() {
        return LOCAL_JDBC_URL;
    }

    @Override
    protected DataSource newDataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL(url());
        dataSource.setUser(username());
        dataSource.setPassword(password());
        return dataSource;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String password() {
        return password;
    }

    @Override
    public JdbcDatabaseContainer newJdbcDatabaseContainer() {
        return new PostgreSQLContainer("postgres:17");
    }
}
