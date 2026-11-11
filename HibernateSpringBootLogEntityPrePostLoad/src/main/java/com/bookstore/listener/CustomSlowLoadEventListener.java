package com.bookstore.listener;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomSlowLoadEventListener implements PreLoadEventListener, PostLoadEventListener {

    private static final Logger log = LoggerFactory.getLogger(CustomSlowLoadEventListener.class);
    private static final long SLOW_LOAD_THRESHOLD_NS = TimeUnit.MILLISECONDS.toNanos(1); // 1 ms

    // Stores start times per entity instance on the current thread
    private final ThreadLocal<Map<Object, Long>> startTimes = ThreadLocal.withInitial(IdentityHashMap::new);

    @Override
    public void onPreLoad(PreLoadEvent event) {        
        startTimes.get().put(event.getEntity(), System.nanoTime());
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Long startTime = startTimes.get().remove(event.getEntity());
        if (startTime == null) {
            return;
        }

        long durationNs = System.nanoTime() - startTime;
        String entityName = event.getPersister().getEntityName();
        Object id = event.getId();

        if (durationNs > SLOW_LOAD_THRESHOLD_NS) {
            log.warn("Slow load of entity: {} with ID: {}. Time: {} ms",
                     entityName, id, (durationNs / 1_000_000.0));
        }
    }
}
