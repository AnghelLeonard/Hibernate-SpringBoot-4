package com.bookstore.dto;

import java.time.Instant;

// the DTO must contains the Sort fields, id and createdAt
public record AuthorDto(Long id, Instant createdAt, String name, String genre) {}
