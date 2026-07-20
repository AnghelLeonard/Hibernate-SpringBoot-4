package com.bookstore.forum;

import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner init(ForumService forumService, HypersistenceOptimizer hypersistenceOptimizer) {
        return args -> {
            forumService.deleteAll();
            forumService.createTag("hibernate");
            forumService.createTag("jpa");

            forumService.createPost(
                "High-Performance Java Persistence", "Vlad Mihalcea",
                List.of("hibernate", "jpa"),
                List.of("Great read", "Very useful"));

            LOGGER.info("Hypersistence Optimizer found {} issues in this mapping:",
                hypersistenceOptimizer.getEvents().size());
            for (Event event : hypersistenceOptimizer.getEvents()) {
                LOGGER.info("  [{}] {} — {} ({})",
                    event.getPriority(), event.getClass().getSimpleName(),
                    event.getDescription(), event.getInfoUrl());
            }
        };
    }
}
