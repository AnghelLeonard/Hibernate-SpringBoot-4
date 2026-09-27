package com.bookstore.dto;

// we don't use a Spring interface because we want to get rid of TupleBackedMap in JSON serialization
public record AuthorDto(String name, int age, long total) {}
