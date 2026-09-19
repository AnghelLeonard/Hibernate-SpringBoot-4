package com.bookstore.generator;

import java.util.EnumSet;
import java.util.Properties;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.type.Type;
import org.springframework.data.mapping.MappingException;

public class CustomPooledLoGenerator extends SequenceStyleGenerator {

    private final String sequenceName;
    private final int initialValue;
    private final int incrementSize;

    public CustomPooledLoGenerator(CustomId configId, GeneratorCreationContext creationContext) {

        sequenceName = configId.sequenceName();
        initialValue = configId.initialValue();
        incrementSize = configId.incrementSize();
    }

    @Override
    public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {

        parameters.put(SEQUENCE_PARAM, sequenceName);                             // "sequence_name"
        parameters.put(INITIAL_PARAM, initialValue);                              // "initial_value"
        parameters.put(INCREMENT_PARAM, incrementSize);                           // "increment_size"
        parameters.put(OPT_PARAM, "org.hibernate.id.enhanced.PooledLoOptimizer"); // "optimizer"
        
        super.configure(creationContext, parameters);
    }

    @Override
    public Type getIdentifierType() {
        
        // when this will be called we can create a Type from Long and return it
        
        return super.getIdentifierType();
    }

    @Override
    public Class<?> getGeneratedType() {
        return String.class;
    }

    @Override
    public EnumSet<EventType> getEventTypes() {
        return EnumSet.of(EventType.INSERT);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        
        Object rawValue = super.generate(session, object);

        if (rawValue instanceof Number number) {           
            return "A-" + String.format("%d", number.longValue());
        }      
        
        return rawValue;
    }
}
