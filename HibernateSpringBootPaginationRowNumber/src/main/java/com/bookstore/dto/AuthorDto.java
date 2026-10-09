package com.bookstore.dto;

// we use a Java record to avoid the 'org.springframework.data.jpa.util.TupleBackedMap' overhead
public record AuthorDto(String name, Integer age, Long total) {}
