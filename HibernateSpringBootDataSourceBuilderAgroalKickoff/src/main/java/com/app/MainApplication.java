package com.app;

import com.app.service.PoolStressTestService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {
   
    private final PoolStressTestService poolStressTestService;

    public MainApplication(PoolStressTestService poolStressTestService) {
        this.poolStressTestService = poolStressTestService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("Start inserting ...");
            poolStressTestService.runConcurrentInserts(1_000);
            System.out.println("Done ...");
        };
    }
}
