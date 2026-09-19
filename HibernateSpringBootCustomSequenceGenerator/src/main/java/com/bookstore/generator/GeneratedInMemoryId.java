package com.bookstore.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(CustomInMemoryIdGenerator.class) 
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface GeneratedInMemoryId {}
