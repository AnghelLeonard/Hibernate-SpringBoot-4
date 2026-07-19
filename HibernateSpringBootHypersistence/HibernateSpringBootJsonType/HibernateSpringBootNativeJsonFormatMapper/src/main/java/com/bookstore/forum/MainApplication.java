package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
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
    public ApplicationRunner init(ForumService forumService) {
        return args -> {
            Post post = new Post("Custom FormatMapper for native JSON");
            post.setProperties(new PostProperties("must-read", true));

            Post loaded = forumService.findById(forumService.save(post).getId());
            LOGGER.info("Loaded flairLabel: {}, pinnedByModerator: {}",
                loaded.getProperties().getFlairLabel(),
                loaded.getProperties().isPinnedByModerator());
        };
    }
}
