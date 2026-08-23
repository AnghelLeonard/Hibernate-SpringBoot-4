package com.bookstore.forum;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner init(HypersistenceOptimizer hypersistenceOptimizer) {
        return args -> LOGGER.info(
            "Hypersistence Optimizer found {} issues after waiving the IDENTITY ones.",
            hypersistenceOptimizer.getEvents().size());
    }
}
