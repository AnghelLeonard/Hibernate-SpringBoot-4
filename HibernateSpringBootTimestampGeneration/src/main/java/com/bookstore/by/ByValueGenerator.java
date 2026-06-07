package com.bookstore.by;

import com.bookstore.service.UserService;
import java.util.EnumSet;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;

public class ByValueGenerator implements BeforeExecutionGenerator {

    public final UserService userService;

    public ByValueGenerator(UserService userService) {
        this.userService = userService;
    }    

    @Override
    public Object generate(SharedSessionContractImplementor ssci, Object o, Object o1, EventType et) {
        // Hook into a service to get the current user, etc.               
        return userService.getCurrentUserName();
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT, EventType.UPDATE);
    }
}
