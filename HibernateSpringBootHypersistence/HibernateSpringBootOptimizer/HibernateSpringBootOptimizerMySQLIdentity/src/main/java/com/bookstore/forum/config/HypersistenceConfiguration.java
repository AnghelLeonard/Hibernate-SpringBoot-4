package com.bookstore.forum.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.EventFilter;
import io.hypersistence.optimizer.hibernate.event.mapping.identifier.IdentityGeneratorEvent;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The bean is the same as before, except for the one waiver MySQL forces on us.
 *
 * <p>Unlike the narrow, attribute-scoped filter of the previous item, this one
 * waives an entire event type. That is the right call here precisely because
 * the constraint is database-wide: MySQL has no sequences, so every entity that
 * owns an identifier has to use {@code IDENTITY}, and every one of them reports
 * an {@code IdentityGeneratorEvent}. There is no per-entity decision to encode —
 * the whole category is unavoidable on this database.</p>
 */
@Configuration
public class HypersistenceConfiguration {

    // tag::filter[]
    private static final EventFilter IGNORE_IDENTITY_ON_MYSQL =
        event -> !(event instanceof IdentityGeneratorEvent);
    // end::filter[]

    // tag::bean[]
    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(EntityManagerFactory entityManagerFactory) {
        return new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
                .setEventFilter(IGNORE_IDENTITY_ON_MYSQL)
        );
    }
    // end::bean[]
}
