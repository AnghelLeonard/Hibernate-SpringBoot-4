package com.citylots;

import com.citylots.batch.JdbcConcurrentBatchService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    // 'citylots.json' is available in the root of the app as a ZIP archive
    // before running the application, unzip this archive
    
    private static final String FILE_NAME = "citylots.json";

    private final JdbcConcurrentBatchService jdbcConcurrentBatchService;

    public MainApplication(JdbcConcurrentBatchService jdbcConcurrentBatchService) {
        this.jdbcConcurrentBatchService = jdbcConcurrentBatchService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            jdbcConcurrentBatchService.fileToDatabase(FILE_NAME);
        };
    }
}
