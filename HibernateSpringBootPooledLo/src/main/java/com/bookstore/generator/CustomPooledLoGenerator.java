package com.bookstore.generator;

import java.util.Properties;
import org.hibernate.MappingException;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class CustomPooledLoGenerator extends SequenceStyleGenerator {
    
    private final String sequenceName;
    private final int initialValue;
    private final int incrementSize;
    
    public CustomPooledLoGenerator(AuthorId configId, GeneratorCreationContext creationContext) {        
        
        sequenceName = configId.sequenceName();       
        initialValue = configId.initialValue();
        incrementSize = configId.incrementSize();
    }

    @Override
    public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {
        
        parameters.put(SEQUENCE_PARAM, sequenceName);                           // "sequence_name"
        parameters.put(INITIAL_PARAM, initialValue);                            // "initial_value"
        parameters.put(INCREMENT_PARAM, incrementSize);                         // "increment_size"
        // or class name, org.hibernate.id.enhanced.PooledLoOptimizer
        parameters.put(OPT_PARAM, "pooled-lo");                                 // "optimizer"
              
        super.configure(creationContext, parameters);
    }            
}
