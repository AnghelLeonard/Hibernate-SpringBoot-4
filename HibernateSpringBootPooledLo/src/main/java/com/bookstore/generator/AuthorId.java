package com.bookstore.generator;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;

@IdGeneratorType(CustomPooledLoGenerator.class)
@Retention( RetentionPolicy.RUNTIME)
@Target({ FIELD, METHOD})
public @interface AuthorId {
    String sequenceName() default "custom_pooled_lo_sequence";
    int initialValue()    default 1;
    int incrementSize()   default 100;
}
