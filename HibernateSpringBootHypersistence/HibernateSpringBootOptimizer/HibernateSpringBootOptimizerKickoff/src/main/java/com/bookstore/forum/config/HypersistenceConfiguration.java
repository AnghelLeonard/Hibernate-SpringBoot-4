package com.bookstore.forum.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The entire integration: one bean. Building it scans the
 * {@link EntityManagerFactory} — every mapping, and the Hibernate configuration
 * behind it — and records an {@code Event} for each performance problem it
 * recognises. The events are then available from
 * {@link HypersistenceOptimizer#getEvents()}, which is what makes them
 * assertable in a test instead of merely printable to a log.
 */
@Configuration
public class HypersistenceConfiguration {

    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(EntityManagerFactory entityManagerFactory) {
        return new HypersistenceOptimizer(new JpaConfig(entityManagerFactory));
    }
}
