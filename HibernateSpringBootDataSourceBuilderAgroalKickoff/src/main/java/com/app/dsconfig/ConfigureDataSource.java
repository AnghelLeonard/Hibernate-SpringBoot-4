package com.app.dsconfig;

import io.agroal.springframework.boot.AgroalDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConfigureDataSource {

    @Bean
    @Primary
    @ConfigurationProperties("app.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("app.datasource")
    public AgroalDataSource dataSource(DataSourceProperties properties) {

        AgroalDataSource hds = new AgroalDataSource();

        hds.setUrl(properties.getUrl());
        hds.setUsername(properties.getUsername());
        hds.setPassword(properties.getPassword());               

        return hds;
    }
}
