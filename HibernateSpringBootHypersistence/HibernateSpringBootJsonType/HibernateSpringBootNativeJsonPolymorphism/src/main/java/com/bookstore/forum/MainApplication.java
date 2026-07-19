package com.bookstore.forum;

import com.bookstore.forum.entity.CodeBlock;
import com.bookstore.forum.entity.ImageBlock;
import com.bookstore.forum.entity.Post;
import com.bookstore.forum.entity.TextBlock;
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
            Post post = new Post("Mapping polymorphic JSON with jsonb")
                .addBlock(new TextBlock("Here is how you store a heterogeneous body:"))
                .addBlock(new CodeBlock("java", "@JdbcTypeCode(SqlTypes.JSON) List<PostContentBlock> body;"))
                .addBlock(new ImageBlock("https://example.com/jsonb.png", "jsonb diagram"));

            Long id = forumService.save(post).getId();
            Post loaded = forumService.findById(id);
            LOGGER.info("Loaded {} blocks, subtypes: {}",
                loaded.getBody().size(),
                loaded.getBody().stream().map(b -> b.getClass().getSimpleName()).toList());
        };
    }
}
