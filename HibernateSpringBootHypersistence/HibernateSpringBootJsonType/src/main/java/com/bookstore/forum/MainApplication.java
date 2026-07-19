package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.PostProperties;
import com.bookstore.forum.service.ForumService;
import tools.jackson.databind.ObjectMapper;
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
            ObjectMapper objectMapper = new ObjectMapper();

            PostProperties properties = new PostProperties("must-read");
            properties.setPinned(true);
            properties.getAttachments().add("table-of-contents.pdf");

            Post post = forumService.newPost(
                "Is Hibernate Spring Boot 4 worth reading?",
                properties
            );

            forumService.addComment(
                post.getId(),
                "Just finished the JSON chapter, excellent!",
                objectMapper.readTree("{\"votes\": 15, \"reactions\": [\"like\", \"insightful\"]}")
            );

            Post loadedPost = forumService.findById(post.getId());
            LOGGER.info("Post flair: {}", loadedPost.getProperties().getFlair());
            LOGGER.info("Post attachments: {}", loadedPost.getProperties().getAttachments());

            List<Long> pinnedPostIds = forumService.findIdsByFlair("must-read");
            LOGGER.info("Posts with the must-read flair: {}", pinnedPostIds);
        };
    }
}
