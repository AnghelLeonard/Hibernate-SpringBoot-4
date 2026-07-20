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
            for (int i = 1; i <= 5; i++) {
                forumService.createPost("Post " + i, "Vlad Mihalcea",
                    List.of("hibernate"), List.of("Great read", "Very useful"));
            }

            LOGGER.info("The mapping itself is clean — {} issue(s) so far.",
                hypersistenceOptimizer.getEvents().size());
            hypersistenceOptimizer.getEvents().clear();

            forumService.findPage(0, 2);
            report("a page without an ORDER BY", hypersistenceOptimizer);

            forumService.findPageOrdered(0, 2);
            report("the same page, ordered", hypersistenceOptimizer);

            forumService.findCommentAuthorsOneByOne();
            report("walking a lazy association in a loop", hypersistenceOptimizer);

            forumService.findCommentAuthorsInOneQuery();
            report("the same data with a join fetch", hypersistenceOptimizer);

            forumService.slowTransaction(200);
            report("a transaction that holds its connection for 200 ms", hypersistenceOptimizer);
        };
    }

    private static void report(String what, HypersistenceOptimizer hypersistenceOptimizer) {
        LOGGER.info("After {}: {} event(s)", what, hypersistenceOptimizer.getEvents().size());
        for (Event event : hypersistenceOptimizer.getEvents()) {
            LOGGER.info("  [{}] {} — {}",
                event.getPriority(), event.getClass().getSimpleName(), event.getDescription());
        }
        hypersistenceOptimizer.getEvents().clear();
    }
}
