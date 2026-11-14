package com.bookstore.config;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.hibernate.integrator.spi.Integrator;
import org.springframework.stereotype.Component;

@Component
public class DatabaseTableMetadataExtractor implements Integrator {

    private Metadata metadata;
    private Database database;

    @Override
    public void integrate(
            Metadata metadata,
            BootstrapContext bootstrapContext,
            SessionFactoryImplementor sessionFactory) {
        this.metadata = metadata;
        this.database = metadata.getDatabase();
    }

    @Override
    public void disintegrate(
            SessionFactoryImplementor sessionFactory,
            SessionFactoryServiceRegistry serviceRegistry) {
        this.metadata = null;
        this.database = null;
    }

    public Metadata getMetadata() {
        if (metadata == null) {
            throw new IllegalStateException("Hibernate has not initialized the Metadata yet.");
        }
        return metadata;
    }

    public Database getDatabase() {
        if (database == null) {
            throw new IllegalStateException("Hibernate has not initialized the Database metadata yet.");
        }
        return database;
    }
}