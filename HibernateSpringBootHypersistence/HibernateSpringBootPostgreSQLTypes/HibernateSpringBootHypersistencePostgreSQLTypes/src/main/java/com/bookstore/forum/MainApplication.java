package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.utils.hibernate.type.range.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

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
            Post post = new Post("Mapping PostgreSQL-specific types with Hypersistence Utils");

            PostDetails details = new PostDetails(post);
            details.setPublicationPeriod(Range.closed(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 23, 59)));
            details.setReadTimeBudget(Duration.ofMinutes(8));
            details.setAttributes(Map.of("difficulty", "advanced", "language", "en"));
            PostDetails savedDetails = forumService.savePostWithDetails(details);

            forumService.savePostWithComment(new PostComment(post, "Great write-up!", "192.168.1.42"));

            forumService.saveTag(new Tag("Hibernate"));

            LOGGER.info("Published-at 2026-06-01: {}",
                forumService.findPublishedAt("2026-06-01 12:00:00").size());
            LOGGER.info("Comments from 192.168.1.0/24: {}",
                forumService.findFromSubnet("192.168.1.0/24").size());
            LOGGER.info("Threads with a 'difficulty' attribute: {}",
                forumService.findWithAttribute("difficulty").size());
            LOGGER.info("Tag lookup 'hibernate' (case-insensitive) present: {}",
                forumService.findTagByName("hibernate").isPresent());
            LOGGER.info("Reloaded read-time budget: {}",
                forumService.findDetails(savedDetails.getId()).getReadTimeBudget());
        };
    }
}
