package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
            Post post = new Post("Mapping PostgreSQL arrays with the native support");
            post.setKeywords(new String[]{"hibernate", "arrays", "postgresql"});
            post.setTopics(List.of("mapping", "types"));
            post.setScores(new Integer[]{5, 4, 5});
            post.setPublicationDates(new LocalDate[]{LocalDate.of(2026, 1, 1)});
            post.setEditorIds(new UUID[]{UUID.randomUUID()});

            Post loaded = forumService.findById(forumService.save(post).getId());
            LOGGER.info("Loaded keywords: {}, topics: {}",
                (Object) loaded.getKeywords(), loaded.getTopics());

            LOGGER.info("Posts tagged 'arrays' (= ANY): {}",
                forumService.findByKeyword("arrays").size());
        };
    }
}
