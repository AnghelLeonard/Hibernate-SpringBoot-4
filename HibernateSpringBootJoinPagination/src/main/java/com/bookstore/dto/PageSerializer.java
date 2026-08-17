package com.bookstore.dto;

import org.springframework.data.domain.PageImpl;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class PageSerializer extends StdSerializer<PageImpl> {
	
	public PageSerializer() {
		super(PageImpl.class);
	}

    @Override
    public void serialize(PageImpl value, JsonGenerator gen, SerializationContext provider) throws JacksonException {
        gen.writeStartObject();
		gen.writeNumberProperty("number", value.getNumber());
		gen.writeNumberProperty("numberOfElements", value.getNumberOfElements());
		gen.writeNumberProperty("totalElements", value.getTotalElements());
		gen.writeNumberProperty("totalPages", value.getTotalPages());
		gen.writeNumberProperty("size", value.getSize());
		//gen.writeName("content");
		provider.defaultSerializeProperty("content", value.getContent(), gen);
		gen.writeEndObject();
    }

	

}
