package com.bookstore.generator;

import java.util.Properties;
import org.hibernate.MappingException;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomIdGenerator extends SequenceStyleGenerator {

    @Override
    public void configure(GeneratorCreationContext creationContext, Properties parameters) throws MappingException {
        
        parameters.put(SEQUENCE_PARAM, "hilo_sequence");                        // "sequence_name"
        parameters.put(INITIAL_PARAM, 1);                                       // "initial_value"
        parameters.put(INCREMENT_PARAM, 100);                                   // "increment_size"
        parameters.put(OPT_PARAM, "org.hibernate.id.enhanced.HiLoOptimizer"); // "optimizer"
              
        super.configure(creationContext, parameters);
    }
    
    
    
}
