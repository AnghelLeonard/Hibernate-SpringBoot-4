package com.bookstore.forum;

import com.bookstore.forum.service.PostService;
import com.bookstore.forum.service.RetryableForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner init(PostService postService, RetryableForumService retryableForumService) {
        return args -> {
            Long id = postService.createPost("Retrying recoverable failures");

            // With no contention, the retryable call succeeds on the first attempt.
            AtomicInteger attempts = new AtomicInteger();
            retryableForumService.incrementLikes(id, 1, attempts);

            LOGGER.info("Likes: {} (took {} attempt(s))", postService.getLikes(id), attempts.get());
        };
    }
}
