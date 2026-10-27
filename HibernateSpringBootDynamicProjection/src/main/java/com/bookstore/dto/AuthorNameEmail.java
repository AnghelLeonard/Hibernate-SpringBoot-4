package com.bookstore.dto;

public interface AuthorNameEmail {

    public String getName();
    public String getEmail();
}

// or, as a Java record
// public record AuthorNameEmail(String name, String email) {}
