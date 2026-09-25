package com.bookstore.listener;

import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomSlowLoadEventListener implements PostLoadEventListener {

    private static final Logger log = LoggerFactory.getLogger(CustomSlowLoadEventListener.class);
        
    private static final long SLOW_LOAD_THRESHOLD_NS = 50_000_000; // 50 ms

    @Override
    public void onPostLoad(PostLoadEvent event) {
        long startTime = System.nanoTime();
       
        Object entity = event.getEntity();
        String entityName = event.getPersister().getEntityName();
        Object id = event.getId();
       
        long durationNs = System.nanoTime() - startTime;
        
        log.info("Loaded entity: {} with ID {} in {} ms", entityName, id, (durationNs / 1_000_000.0));
        
        if (durationNs > SLOW_LOAD_THRESHOLD_NS) {
            log.info("Slow load of entity: {} with ID: {}. Time: {} ms", 
                     entityName, id, (durationNs / 1_000_000.0));
        }
    }
}