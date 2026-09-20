package com.bookstore.generator;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class CustomUUIDv7Generator extends SequenceStyleGenerator {
        
   @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        return UUIDv7.randomUUID();
    }
}