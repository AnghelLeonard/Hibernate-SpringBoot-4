/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore;

import com.bookstore.dto.TupleBackedMapSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.util.TupleBackedMap;

import tools.jackson.databind.module.SimpleModule;

/**
 *
 * @author leopr
 */
@Configuration
public class SpringApplicationConfiguration {



	// @see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-customize-the-jackson-objectmapper
	/*@Bean
    public SimpleModule tupleBackedMapModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(TupleBackedMap.class, new TupleBackedMapSerializer());
        return module;
    }*/
	
}