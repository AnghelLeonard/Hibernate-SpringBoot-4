package com.bookstore.config;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.spi.BootstrapContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class DatabaseTableMetadataExtractor
        implements org.hibernate.integrator.spi.Integrator {

    public static final DatabaseTableMetadataExtractor EXTRACTOR
            = new DatabaseTableMetadataExtractor();

    private Database database;       
    
    @Override
    public void integrate(Metadata metadata, 
            BootstrapContext bootstrapContext, 
            SessionFactoryImplementor sessionFactory) {

        database = metadata.getDatabase();
    }

    @Override
    public void disintegrate(
            SessionFactoryImplementor sessionImplementor,
            SessionFactoryServiceRegistry serviceRegistry) {
    }
    
    public Database getDatabase() {
        return database;
    }
}
