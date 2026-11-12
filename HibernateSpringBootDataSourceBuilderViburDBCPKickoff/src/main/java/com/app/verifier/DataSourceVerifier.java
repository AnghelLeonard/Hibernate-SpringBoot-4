package com.app.verifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vibur.dbcp.ViburDBCPDataSource;

@Configuration
public class DataSourceVerifier {

    @Bean
    public CommandLineRunner verifyDataSource(javax.sql.DataSource dataSource) {
        return args -> {
            System.out.println("=== Active DataSource Class: " + dataSource.getClass().getName() + " ===");
            
            if (dataSource instanceof ViburDBCPDataSource viburPool) {
                System.out.println("=== Pool Name: " + viburPool.getName() + " ===");
                System.out.println("=== Max Pool Size: " + viburPool.getPoolMaxSize() + " ===");
            }
        };
    }
}

