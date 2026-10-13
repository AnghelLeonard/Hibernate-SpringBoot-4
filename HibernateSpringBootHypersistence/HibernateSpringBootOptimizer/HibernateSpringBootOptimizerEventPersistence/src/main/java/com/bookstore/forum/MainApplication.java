package com.bookstore.forum;

import com.bookstore.forum.service.ForumService;
import io.hypersistence.optimizer.HypersistenceOptimizer;
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

    /**
     * Seeds a handful of posts, then runs the two scenarios that produce runtime
     * events — a large result list and an N+1 query — so the web app has
     * something to show. The web app URL is logged last; open it in a browser to
     * see the Timeline, Events, and Runtime views. The application keeps running
     * so the web app stays reachable.
     */
    @Bean
    @Profile("!test")
    public ApplicationRunner init(ForumService forumService, HypersistenceOptimizer hypersistenceOptimizer) {
        return args -> {
            forumService.deleteAll();
            for (int i = 1; i <= 5; i++) {
                forumService.createPost("Post " + i, List.of("Great read", "Very useful"));
            }

            forumService.findComments();
            forumService.findCommentAuthorsOneByOne();

            hypersistenceOptimizer.getEventPersistenceContext().flushRuntimeEvents();

            LOGGER.info("Open the Event Persistence Web App at {}", hypersistenceOptimizer.getWebAppUrl());
        };
    }
}
