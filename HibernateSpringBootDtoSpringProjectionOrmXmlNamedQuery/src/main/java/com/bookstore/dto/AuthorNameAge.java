package com.bookstore.dto;

public interface AuthorNameAge {
    
    public String getName();
    public Integer getAge();   
}

// or, as a Java record
// public record AuthorNameAge(Integer age, String name) {}