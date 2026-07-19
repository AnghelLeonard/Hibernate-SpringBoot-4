package com.bookstore.forum.config;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.SQLServerDialect;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MSSQLServerContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Resolves the test {@link DataSource} using the local SQL Server instance
 * first and, only if that's not reachable, falls back to a reusable
 * Testcontainers instance. The missing {@code bookstoredb} database is
 * created through the {@code master} database on first use.
 */
public class SQLServerDataSourceProvider extends AbstractContainerDataSourceProvider {

    private static final String LOCAL_JDBC_URL =
        "jdbc:sqlserver://localhost;instance=SQLEXPRESS;databaseName=bookstoredb;encrypt=true;trustServerCertificate=true";

    private static final String MAINTENANCE_JDBC_URL =
        "jdbc:sqlserver://localhost;instance=SQLEXPRESS;databaseName=master;encrypt=true;trustServerCertificate=true";

    private static final String[][] LOCAL_CREDENTIALS = {
        {"sa", "adm1n"},
        {"sa", "root"}
    };

    public static final SQLServerDataSourceProvider INSTANCE = new SQLServerDataSourceProvider();

    private final String username;
    private final String password;

    private SQLServerDataSourceProvider() {
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
                statement.executeUpdate(
                    "if db_id('bookstoredb') is null create database bookstoredb"
                );
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
        return Database.SQLSERVER;
    }

    @Override
    public String hibernateDialect() {
        return SQLServerDialect.class.getName();
    }

    @Override
    protected String defaultJdbcUrl() {
        return LOCAL_JDBC_URL;
    }

    @Override
    protected DataSource newDataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setURL(url());
        JdbcDatabaseContainer container = getContainer();
        if (container == null) {
            dataSource.setUser(username());
            dataSource.setPassword(password());
        } else {
            dataSource.setUser(container.getUsername());
            dataSource.setPassword(container.getPassword());
        }
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
        return new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2022-latest");
    }

    @Override
    public boolean supportsDatabaseName() {
        return false;
    }

    @Override
    public boolean supportsCredentials() {
        return false;
    }
}
