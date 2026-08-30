/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.jpa.util.TupleBackedMap;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

public class TupleBackedMapSerializer extends StdSerializer<TupleBackedMap> {
	
	public TupleBackedMapSerializer() {
		super(TupleBackedMap.class);
	}

    @Override
    public void serialize(TupleBackedMap value, JsonGenerator gen, SerializationContext provider) throws JacksonException {
        if (value == null) {
            gen.writeNull();
        } else {
            Map<String, Object> standardMap = new LinkedHashMap<>(value);
            
            System.out.println("x="+standardMap);;
            
            // Unwraps the proxy map structure into a standard map
            gen.writeStartObject(standardMap);
            gen.writeEndObject();
        }
    }
}
	
