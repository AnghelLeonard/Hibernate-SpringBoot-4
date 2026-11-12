package com.app.verifier;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

@Configuration
public class DataSourceVerifier {

    @Bean
    public CommandLineRunner verifyDataSource(DataSource dataSource) {
        return args -> {
            System.out.println("=== Active DataSource Class: " + dataSource.getClass().getName() + " ===");
            
            if (dataSource instanceof BasicDataSource dbcp2) {
                System.out.println("=== DBCP2: " + dbcp2);
                System.out.println("=== DBCP2 Max Total: " + dbcp2.getMaxTotal() + " ===");
                System.out.println("=== DBCP2 Initial Size: " + dbcp2.getInitialSize() + " ===");
            }
        };
    }
}

