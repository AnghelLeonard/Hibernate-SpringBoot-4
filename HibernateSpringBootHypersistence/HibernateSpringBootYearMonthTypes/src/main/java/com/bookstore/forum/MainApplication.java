package com.bookstore.forum;

import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.YearMonth;

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
            PostDetails details = new PostDetails("Mapping YearMonth on MySQL");
            details.setPublishedOn(YearMonth.of(2026, 7));
            details.setArchivedOn(YearMonth.of(2026, 12));

            PostDetails loaded = forumService.findById(forumService.save(details).getId());
            LOGGER.info("Published on (integer-mapped): {}", loaded.getPublishedOn());
            LOGGER.info("Archived on (date-mapped): {}", loaded.getArchivedOn());
            LOGGER.info("Threads published in 2026-07: {}",
                forumService.findByPublishedOn(YearMonth.of(2026, 7)).size());
        };
    }
}
