package com.app.verifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.tomcat.jdbc.pool.DataSource;

@Configuration
public class DataSourceVerifier {

    @Bean
    public CommandLineRunner verifyDataSource(javax.sql.DataSource dataSource) {
        return args -> {
            System.out.println("=== Active DataSource Class: " + dataSource.getClass().getName() + " ===");
            
            if (dataSource instanceof DataSource tomcatPool) {
                System.out.println("=== Pool Name: " + tomcatPool.getPoolName() + " ===");
                System.out.println("=== Max Active: " + tomcatPool.getMaxActive() + " ===");
            }
        };
    }
}

