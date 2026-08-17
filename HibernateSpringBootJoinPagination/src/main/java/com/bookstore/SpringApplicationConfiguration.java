/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bookstore;

import com.bookstore.dto.PageSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageImpl;
import tools.jackson.core.Version;

import tools.jackson.databind.module.SimpleModule;

/**
 *
 * @author leopr
 */
@Configuration
public class SpringApplicationConfiguration {



	// @see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-customize-the-jackson-objectmapper
	@Bean
	public SimpleModule jacksonPageWithJsonViewModule() {
		SimpleModule module = new SimpleModule("jackson-page-with-jsonview", Version.unknownVersion());
		module.addSerializer(PageImpl.class, new PageSerializer());
		return module;
	}
	
}