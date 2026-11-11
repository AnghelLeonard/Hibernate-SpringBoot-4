package com.bookstore.config;

import com.bookstore.listener.EntityCreationEventListener;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateEventListenerConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityCreationEventListener entityCreationEventListener;

    public HibernateEventListenerConfig(EntityManagerFactory entityManagerFactory,
                                        EntityCreationEventListener entityCreationEventListener) {
        this.entityManagerFactory = entityManagerFactory;
        this.entityCreationEventListener = entityCreationEventListener;
    }

    @PostConstruct
    public void registerListeners() {
        SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                .getService(EventListenerRegistry.class);

        if (registry != null) {
            registry.appendListeners(EventType.PRE_LOAD, entityCreationEventListener);
            registry.appendListeners(EventType.POST_LOAD, entityCreationEventListener);
        }
    }
}

