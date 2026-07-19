package com.bookstore.forum.config;

import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * Overrides the auto-configured {@link DataSource} with one resolved by the
 * {@link MySQLDataSourceProvider}, wrapped with datasource-proxy so that
 * {@code SQLStatementCountValidator} can assert the executed statement counts.
 */
@TestConfiguration
public class TestDataSourceConfiguration {

    @Bean
    public DataSource dataSource() {
        return ProxyDataSourceBuilder
            .create(MySQLDataSourceProvider.INSTANCE.dataSource())
            .name("DATA_SOURCE_PROXY")
            .countQuery()
            .build();
    }
}
