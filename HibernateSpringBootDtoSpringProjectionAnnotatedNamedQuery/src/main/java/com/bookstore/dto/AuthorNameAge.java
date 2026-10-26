package com.bookstore.dto;

public interface AuthorNameAge {
    
    public String getName();
    public Integer getAge();   
}

// or, Java record
// public record AuthorNameAge(Integer age, String name) {}
