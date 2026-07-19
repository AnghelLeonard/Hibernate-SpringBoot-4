package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.tsid.TSID;
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
    public ApplicationRunner init(ForumService forumService) {
        return args -> {
            Post post = forumService.newPost(
                "Is Hibernate Spring Boot 4 worth reading?",
                List.of("hibernate", "jpa")
            );

            TSID tsid = TSID.from(post.getId());

            LOGGER.info("Post identifier: {}", post.getId());
            LOGGER.info("Post identifier, Crockford base32: {}", tsid);
            LOGGER.info("Post identifier creation time: {}", tsid.getInstant());

            forumService.insertPosts(30);

            LOGGER.info("Total posts: {}", forumService.countPosts());
        };
    }
}
