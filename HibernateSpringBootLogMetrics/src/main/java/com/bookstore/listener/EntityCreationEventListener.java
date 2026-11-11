package com.bookstore.listener;

import com.bookstore.context.QueryMetricsContext;
import java.util.IdentityHashMap;
import java.util.Map;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.hibernate.event.spi.PreLoadEvent;
import org.hibernate.event.spi.PreLoadEventListener;
import org.springframework.stereotype.Component;

@Component
public class EntityCreationEventListener implements PreLoadEventListener, PostLoadEventListener {

    private final ThreadLocal<Map<Object, Long>> startTimes = ThreadLocal.withInitial(IdentityHashMap::new);

    @Override
    public void onPreLoad(PreLoadEvent event) {
        if (QueryMetricsContext.get() != null) {
            startTimes.get().put(event.getEntity(), System.nanoTime());
        }
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        Map<Object, Long> map = startTimes.get();
        Long startTime = map.remove(event.getEntity());
        if (map.isEmpty()) {
            startTimes.remove();
        }

        if (startTime == null) {
            return;
        }

        long elapsed = System.nanoTime() - startTime;
        QueryMetricsContext.Metrics metrics = QueryMetricsContext.get();
        if (metrics != null) {
            metrics.entityCreationNs += elapsed;
            metrics.entityCount++;
        }
    }
}
