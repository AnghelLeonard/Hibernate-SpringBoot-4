package com.bookstore.dto;

public interface AuthorDto {

    public String getName();
    public Integer getAge();
}

// or as a Java Record
// public record AuthorDto(String name, Integer age) {}
