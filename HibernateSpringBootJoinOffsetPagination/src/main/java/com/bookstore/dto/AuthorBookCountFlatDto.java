package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record AuthorBookCountFlatDto(String name, int age, String title, String isbn, @JsonIgnore long total) {}
