package com.bookstore.forum;

import com.bookstore.forum.entity.BatchedPost;
import com.bookstore.forum.service.ForumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;
import java.util.stream.IntStream;

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
            LOGGER.info("OLTP insert (allocationSize = 1) -> id {}",
                forumService.createPost("A single post, one nextval").getId());

            LOGGER.info("Database script calling nextval itself -> id {}",
                forumService.insertPostAsADatabaseScriptWould("Inserted by an ETL job"));

            List<BatchedPost> imported = forumService.importPosts(
                IntStream.rangeClosed(1, 10).mapToObj(i -> "Imported post " + i).toList());
            LOGGER.info("Batch import of {} posts (one sequence roundtrip) -> ids {}",
                imported.size(), imported.stream().map(BatchedPost::getId).toList());

            LOGGER.info("All identifiers of batch_seq_post, in order: {}",
                forumService.findAllPostsOrderedById().stream().map(p -> p.getId()).toList());

            LOGGER.info("Pooled optimizer insert -> id {}",
                forumService.createPooledPost("Pooled").getId());
        };
    }
}
