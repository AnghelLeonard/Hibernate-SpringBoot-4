package com.bookstore.forum;

import com.bookstore.forum.entity.Post;
import com.bookstore.forum.service.ForumService;
import io.hypersistence.utils.spring.repository.BaseJpaRepositoryImpl;
import jakarta.persistence.LockModeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

/**
 * Wiring the Hypersistence Utils {@code BaseJpaRepository}: point Spring Data at
 * {@link BaseJpaRepositoryImpl} as the repository base class. Every repository
 * in scope then gets the explicit persist/merge/update contract instead of the
 * standard {@code save()}/{@code findAll()} surface.
 */
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class MainApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    @Profile("!test")
    public ApplicationRunner init(ForumService forumService) {
        return args -> {
            Post post = forumService.persist(new Post("High-Performance Java Persistence"));

            forumService.persistAll(List.of(
                new Post("Hypersistence Optimizer"),
                new Post("Hypersistence Utils"),
                new Post("SQL Performance Explained")));

            // Detached update -> direct UPDATE, no SELECT.
            Post detached = new Post("High-Performance Java Persistence, 2nd edition");
            detached.setId(post.getId());
            forumService.update(detached);

            Post locked = forumService.lockById(post.getId(), LockModeType.PESSIMISTIC_WRITE);
            LOGGER.info("Locked post title: {}", locked.getTitle());
        };
    }
}
