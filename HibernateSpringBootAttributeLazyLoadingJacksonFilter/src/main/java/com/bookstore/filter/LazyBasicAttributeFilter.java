package com.bookstore.filter;

import org.hibernate.Hibernate;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.ser.PropertyWriter;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;

public class LazyBasicAttributeFilter extends SimpleBeanPropertyFilter {
    
    @Override
    public void serializeAsProperty(Object pojo, JsonGenerator g, 
            SerializationContext provider, PropertyWriter writer) 
            throws Exception {
        
        if (writer instanceof BeanPropertyWriter beanWriter) {
            String propertyName = beanWriter.getName();
            
            // use Hibernate's metadata check to see if the property is initialized          
            if (!Hibernate.isPropertyInitialized(pojo, propertyName)) {
          
                writer.serializeAsOmittedProperty(pojo, g, provider);
                return;
            }
        }
    
        writer.serializeAsProperty(pojo, g, provider);
    }
}