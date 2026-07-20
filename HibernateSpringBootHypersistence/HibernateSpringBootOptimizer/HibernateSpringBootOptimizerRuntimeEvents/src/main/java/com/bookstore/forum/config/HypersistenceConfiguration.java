package com.bookstore.forum.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Unchanged from the previous item — the same single bean. Only the mappings and
 * the configuration around it changed, and that is the whole point: the tool
 * stays put while the model gets better.
 */
@Configuration
public class HypersistenceConfiguration {

    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(EntityManagerFactory entityManagerFactory) {
        return new HypersistenceOptimizer(new JpaConfig(entityManagerFactory));
    }
}
