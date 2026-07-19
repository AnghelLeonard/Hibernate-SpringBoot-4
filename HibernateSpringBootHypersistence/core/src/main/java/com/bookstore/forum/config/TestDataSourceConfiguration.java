package com.bookstore.forum.config;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Overrides the auto-configured {@link DataSource} with one resolved by the
 * {@link DatabaseType}'s provider, wrapped with datasource-proxy so that
 * {@code SQLStatementCountValidator} can assert the executed statement counts.
 *
 * <p>The target database defaults to MySQL and can be switched per test with
 * {@code @SpringBootTest(properties = "test.database=POSTGRESQL")}. Hibernate
 * auto-detects the dialect from the resolved JDBC connection, so the same
 * entity mappings can run against any of the supported databases.</p>
 */
@TestConfiguration
public class TestDataSourceConfiguration {

    @Value("${test.database:MYSQL}")
    private DatabaseType databaseType;

    @Bean
    public DataSource dataSource() {
        return ProxyDataSourceBuilder
            .create(databaseType.provider().dataSource())
            .name("DATA_SOURCE_PROXY")
            .countQuery()
            .build();
    }
}
