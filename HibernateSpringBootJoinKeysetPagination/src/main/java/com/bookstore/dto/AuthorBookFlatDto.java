package com.bookstore.dto;

public record AuthorBookFlatDto(Long authorId, String name, Integer age, 
        Long bookId, String title, String isbn) {}
