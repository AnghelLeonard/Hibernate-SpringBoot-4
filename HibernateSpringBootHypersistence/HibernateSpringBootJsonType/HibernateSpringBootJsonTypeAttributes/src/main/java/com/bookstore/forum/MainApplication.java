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
import tools.jackson.databind.ObjectMapper;

import java.util.List;
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
            ObjectMapper objectMapper = new ObjectMapper();

            Post post = new Post("Is Hibernate Spring Boot 4 worth reading?");
            post.setProperties(new PostProperties("must-read"));
            post.setMetadata(objectMapper.readTree("{\"votes\": 15}"));
            post.setAttributes(Map.of("locale", "en"));
            post.setRawPayload("{\"source\": \"web\"}");
            post.setTags(List.of("hibernate", "jpa", "json"));

            Post saved = forumService.save(post);

            Post loaded = forumService.findById(saved.getId());
            LOGGER.info("POJO flair: {}", loaded.getProperties().getFlair());
            LOGGER.info("JsonNode metadata: {}", loaded.getMetadata());
            LOGGER.info("Map attributes: {}", loaded.getAttributes());
            LOGGER.info("String raw payload: {}", loaded.getRawPayload());
            LOGGER.info("List tags: {}", loaded.getTags());
        };
    }
}
