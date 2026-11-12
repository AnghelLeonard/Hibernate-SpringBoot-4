package com.app.verifier;

import io.agroal.springframework.boot.AgroalDataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class DataSourceVerifier {

    @Bean
    public CommandLineRunner verifyDataSource(DataSource dataSource) {
        return args -> {
            System.out.println("=== Active DataSource Class: " + dataSource.getClass().getName() + " ===");
            
            if (dataSource instanceof AgroalDataSource agroalCP) {
                System.out.println("=== Agroal Pool: " + agroalCP + " ===");                
            }
        };
    }
}

