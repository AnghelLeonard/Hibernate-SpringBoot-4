package com.app.dsconfig;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureDataSource {

    @Bean
    public HikariDataSource dataSource() {

        HikariDataSource hds = new HikariDataSource();

        hds.setJdbcUrl("jdbc:postgresql://localhost:5432/bookstoredb");
        hds.setUsername("postgres");
        hds.setPassword("root");
        
        // Pool Sizing (Fixed Size)
        hds.setMaximumPoolSize(10);
        hds.setMinimumIdle(10);
        hds.setPoolName("MyAppPool");
        
        // Timeouts (in milliseconds)
        hds.setConnectionTimeout(3000);
        hds.setValidationTimeout(1000);
        hds.setMaxLifetime(1740000);
        hds.setKeepaliveTime(300000);

        // Behavior & Diagnostics
        hds.setAutoCommit(false);
        hds.setLeakDetectionThreshold(2000);        

        return hds;
    }
}
