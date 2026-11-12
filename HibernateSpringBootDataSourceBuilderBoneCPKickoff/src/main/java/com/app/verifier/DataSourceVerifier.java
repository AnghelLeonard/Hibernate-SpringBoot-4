package com.app.verifier;

import com.jolbox.bonecp.BoneCPDataSource;
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
            
            if (dataSource instanceof BoneCPDataSource boneCP) {
                System.out.println("=== BoneCP Pool Name: " + boneCP.getPoolName() + " ===");
                System.out.println("=== Max Connections Per Partition: " + boneCP.getMaxConnectionsPerPartition() + " ===");
            }
        };
    }
}

