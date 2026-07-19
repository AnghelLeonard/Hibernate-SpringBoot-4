package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostComment;
import com.bookstore.forum.entity.PostDetails;
import com.bookstore.forum.entity.Tag;
import com.bookstore.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.net.InetAddress;
import java.time.Duration;

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
            Post post = new Post("Mapping PostgreSQL-specific types natively");

            PostDetails details = new PostDetails(post);
            details.setReadTimeBudget(Duration.ofMinutes(8));
            PostDetails savedDetails = forumService.savePostWithDetails(details);

            forumService.savePostWithComment(
                new PostComment(post, "Great write-up!", InetAddress.getByName("192.168.1.42")));

            forumService.saveTag(new Tag("Hibernate"));

            LOGGER.info("Comments from 192.168.1.0/24 (native inet): {}",
                forumService.findFromSubnet("192.168.1.0/24").size());
            LOGGER.info("Tag lookup 'hibernate' via derived query (case-SENSITIVE, native varchar bind) present: {}",
                forumService.findTagByName("hibernate").isPresent());
            LOGGER.info("Tag lookup 'hibernate' via CAST(? AS citext) (case-insensitive) present: {}",
                forumService.findTagByNameCaseInsensitively("hibernate").isPresent());
            LOGGER.info("Reloaded read-time budget (native interval second): {}",
                forumService.findDetails(savedDetails.getId()).getReadTimeBudget());
        };
    }
}
