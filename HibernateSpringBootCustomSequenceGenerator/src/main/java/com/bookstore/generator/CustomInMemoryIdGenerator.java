package com.bookstore.generator;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicLong;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.EventTypeSets;
import org.springframework.stereotype.Component;

@Component
public class CustomInMemoryIdGenerator implements BeforeExecutionGenerator {

    private static final String PREFIX = "A-";
    
    // Starts at 1. Adjust the initial value if needed.
    private static final AtomicLong COUNTER = new AtomicLong(1);

    @Override
    public Object generate(SharedSessionContractImplementor session, 
            Object owner, Object currentValue, EventType eventType) {
        
        // Increment the counter safely in memory
        long nextValue = COUNTER.getAndIncrement();

        // Format the ID, e.g., 1 becomes "A-0001"
        return PREFIX + String.format("%04d", nextValue);
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EventTypeSets.INSERT_ONLY;
    }
}
