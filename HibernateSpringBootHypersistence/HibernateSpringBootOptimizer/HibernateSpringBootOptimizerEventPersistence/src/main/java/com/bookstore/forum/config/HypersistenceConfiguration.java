package com.bookstore.forum.config;

import io.hypersistence.optimizer.HypersistenceOptimizer;
import io.hypersistence.optimizer.core.config.Config;
import io.hypersistence.optimizer.core.config.JpaConfig;
import io.hypersistence.optimizer.core.event.persistence.SchemaAction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * The same single bean as before, now with the Event Persistence feature turned
 * on. Instead of only logging the detected events, the Optimizer stores them in
 * the same database it analyzes — reusing the host Hibernate {@code SessionFactory},
 * so no second {@code DataSource} or JDBC URL is needed — and starts the
 * embedded Event Persistence Web App so we can browse them.
 *
 * <p>The configuration lives on the {@link Config.EventPersistence} object:</p>
 * <ul>
 *   <li>{@code setApplicationName} labels every stored row, so one event
 *       database can hold the history of several applications or nodes;</li>
 *   <li>{@code setSchemaAction(CREATE)} creates the three {@code ho_*} tables if
 *       they are missing, preserving any existing data — it is not a migration
 *       tool;</li>
 *   <li>{@code setRuntimeRollupWindowMillis} shrinks the rollup window to a few
 *       seconds so the runtime events show up quickly while we experiment; in
 *       production the 5-minute default keeps the table small;</li>
 *   <li>{@code setWebAppEnabled(true)} starts the web app inside this
 *       application. With the port left at {@code 0}, a free port is allocated
 *       and the resolved URL is logged at startup and readable through
 *       {@link HypersistenceOptimizer#getWebAppUrl()}.</li>
 * </ul>
 */
@Configuration
public class HypersistenceConfiguration {

    @Bean
    public HypersistenceOptimizer hypersistenceOptimizer(EntityManagerFactory entityManagerFactory) {
        // tag::event-persistence[]
        return new HypersistenceOptimizer(
            new JpaConfig(entityManagerFactory)
                .setEventPersistence(
                    new Config.EventPersistence()
                        .setEnabled(true)
                        .setApplicationName("forum")
                        .setSchemaAction(SchemaAction.CREATE)
                        .setRuntimeRollupWindowMillis(TimeUnit.SECONDS.toMillis(5))
                        .setWebAppEnabled(true)
                )
        );
        // end::event-persistence[]
    }
}
