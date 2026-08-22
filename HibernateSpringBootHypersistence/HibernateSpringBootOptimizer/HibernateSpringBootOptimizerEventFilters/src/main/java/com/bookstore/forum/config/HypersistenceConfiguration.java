package com.bookstore.forum.config;

import com.bookstore.forum.entity.Post;
import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.ChainEventHandler;
import io.hypersistence.optimizer.core.event.EventFilter;
import io.hypersistence.optimizer.core.event.ListEventHandler;
import io.hypersistence.optimizer.core.event.LogEventHandler;
import io.hypersistence.optimizer.hibernate.event.mapping.EntityAttributeMappingEvent;
import io.hypersistence.optimizer.hibernate.event.mapping.basic.EnumTypeStringEvent;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * The configuration a real project ends up with, once the tool stops being a
 * curiosity and starts gating the build.
 *
 * <p>Two pieces matter here:</p>
 * <ul>
 *   <li>a {@link ChainEventHandler} — the {@link ListEventHandler} keeps the
 *       events so a test can assert on them, while the {@link LogEventHandler}
 *       still prints them for whoever is reading the startup log;</li>
 *   <li>an {@link EventFilter} — the single documented exception this code base
 *       has agreed to live with. Everything else must be zero.</li>
 * </ul>
 *
 * <p>The filter is deliberately narrow: it names an entity and an attribute,
 * not an event type. Suppressing {@code EnumTypeStringEvent} wholesale would
 * hide the next enum somebody maps as a String by accident — which is the one
 * you actually wanted to catch.</p>
 */
@Configuration
public class HypersistenceConfiguration {

    /**
     * {@code Post.status} is an {@code EnumType.STRING} mapping over a legacy
     * {@code VARCHAR} column that other applications share, so it cannot become
     * an ordinal. Reviewed, justified, and accepted — see {@link Post#getStatus()}.
     */
    // tag::filter[]
    private static final EventFilter ACCEPTED_TRADE_OFFS = event ->
        !(event instanceof EnumTypeStringEvent enumTypeString &&
          isAttribute(enumTypeString, Post.class, "status"));

    private static boolean isAttribute(
            EntityAttributeMappingEvent event,
            Class<?> entityClass,
            String attribute) {
        return entityClass.equals(event.getEntityClass()) &&
               attribute.equals(event.getEntityAttribute());
    }
    // end::filter[]

    /**
     * Exposed as a bean so the CI test can read the events straight from the
     * handler that collected them.
     */
    @Bean
    public ListEventHandler listEventHandler() {
        return new ListEventHandler();
    }

    // tag::handlers[]
    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(
            EntityManagerFactory entityManagerFactory,
            ListEventHandler listEventHandler) {
        return new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
                .setEventFilter(ACCEPTED_TRADE_OFFS)
                .setEventHandler(new ChainEventHandler(
                    List.of(listEventHandler, new LogEventHandler()))
                )
        );
    }
    // end::handlers[]
}
