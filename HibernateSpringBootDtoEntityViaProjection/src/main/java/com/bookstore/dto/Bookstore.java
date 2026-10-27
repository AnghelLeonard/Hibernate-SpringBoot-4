package com.bookstore.dto;

import com.bookstore.entity.Author;

public interface Bookstore {

    public Author getAuthor();
    public String getTitle();
}

// or, a Java record
// public record Bookstore(Author author, String title) {}
