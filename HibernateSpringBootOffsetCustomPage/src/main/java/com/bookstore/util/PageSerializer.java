package com.bookstore.util;

import org.springframework.data.domain.PageImpl;
import tools.jackson.core.JsonGenerator;
import org.springframework.boot.jackson.JacksonComponent;
import org.springframework.boot.jackson.ObjectValueSerializer;
import tools.jackson.databind.SerializationContext;

@JacksonComponent
public class PageSerializer extends ObjectValueSerializer<PageImpl<?>> {
  
    @Override
    protected void serializeObject(PageImpl<?> page, JsonGenerator gen, SerializationContext context) {
       
        gen.writePOJOProperty("data", page.getContent());
       
        gen.writeObjectPropertyStart("pagination");
        gen.writeNumberProperty("currentPage", page.getNumber());
        gen.writeNumberProperty("pageSize", page.getSize());
        gen.writeNumberProperty("totalElements", page.getTotalElements());
        gen.writeNumberProperty("totalPages", page.getTotalPages());
        gen.writeBooleanProperty("isFirst", page.isFirst());
        gen.writeBooleanProperty("isLast", page.isLast());
        gen.writeBooleanProperty("hasNet", page.hasNext());
        gen.writeBooleanProperty("hasPrevious", page.hasPrevious());        
        gen.writeEndObject();
    }
}