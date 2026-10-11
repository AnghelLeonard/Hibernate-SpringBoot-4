package com.bookstore.dto;

import java.util.List;

public record AuthorDto (Long id, String name, Integer age, List<BookDto> books) {}
