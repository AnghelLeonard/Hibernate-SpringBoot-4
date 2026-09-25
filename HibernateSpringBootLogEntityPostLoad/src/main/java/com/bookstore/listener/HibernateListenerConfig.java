package com.bookstore.listener;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateListenerConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final CustomSlowLoadEventListener slowLoadEventListener;

    public HibernateListenerConfig(EntityManagerFactory entityManagerFactory, 
                                   CustomSlowLoadEventListener slowLoadEventListener) {
        this.entityManagerFactory = entityManagerFactory;
        this.slowLoadEventListener = slowLoadEventListener;
    }

    @PostConstruct
    public void registerListeners() {
        SessionFactoryImpl sessionFactory = entityManagerFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                                                        .getService(EventListenerRegistry.class);
              
        registry.getEventListenerGroup(EventType.POST_LOAD)
                .appendListener(slowLoadEventListener);
    }
}