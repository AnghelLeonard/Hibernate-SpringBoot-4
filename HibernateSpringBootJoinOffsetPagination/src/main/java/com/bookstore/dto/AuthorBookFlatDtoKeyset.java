package com.bookstore.dto;

import java.time.Instant;

public record AuthorBookFlatDtoKeyset(
        Long id, String name, Integer age, Instant createdAt, String title, String isbn) {}
