package com.bookstore.dto;

import java.util.List;

public record AuthorBookDto(List<AuthorDto> authors, Long total) {}
