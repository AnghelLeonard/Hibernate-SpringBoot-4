package com.bookstore.forum.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import io.hypersistence.utils.test.providers.AbstractContainerDataSourceProvider;
import org.hibernate.dialect.Database;
import org.hibernate.dialect.MySQLDialect;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Resolves the test {@link DataSource} using the local MySQL server first and,
 * only if that's not reachable, falls back to a reusable Testcontainers instance.
 * Both conventional local credential pairs are probed, so the tests run
 * unchanged whether the local server was set up for this book (root/root)
 * or for Hibernate Spring Boot 4 (mysql/admin).
 */
public class MySQLDataSourceProvider extends AbstractContainerDataSourceProvider {

    private static final String LOCAL_JDBC_URL =
        "jdbc:mysql://localhost:3306/bookstoredb?createDatabaseIfNotExist=true";

    private static final String[][] LOCAL_CREDENTIALS = {
        {"root", "root"},
        {"mysql", "admin"}
    };

    public static final MySQLDataSourceProvider INSTANCE = new MySQLDataSourceProvider();

    private final String username;
    private final String password;

    private MySQLDataSourceProvider() {
        String[] credentials = resolveLocalCredentials();
        this.username = credentials[0];
        this.password = credentials[1];
    }

    private static String[] resolveLocalCredentials() {
        for (String[] credentials : LOCAL_CREDENTIALS) {
            try (Connection connection = DriverManager.getConnection(
                    LOCAL_JDBC_URL, credentials[0], credentials[1])) {
                return credentials;
            } catch (SQLException ignore) {
            }
        }
        return LOCAL_CREDENTIALS[0];
    }

    @Override
    public Database database() {
        return Database.MYSQL;
    }

    @Override
    public String hibernateDialect() {
        return MySQLDialect.class.getName();
    }

    @Override
    protected String defaultJdbcUrl() {
        return LOCAL_JDBC_URL;
    }

    @Override
    protected DataSource newDataSource() {
        try {
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setURL(url());
            dataSource.setUser(username());
            dataSource.setPassword(password());
            dataSource.setRewriteBatchedStatements(true);
            return dataSource;
        } catch (SQLException e) {
            throw new IllegalStateException("The DataSource could not be instantiated!", e);
        }
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
        return new MySQLContainer("mysql:8.4");
    }
}
