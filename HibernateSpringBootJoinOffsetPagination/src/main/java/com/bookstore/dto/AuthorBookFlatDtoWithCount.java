package com.bookstore.dto;

import java.util.List;

public record AuthorBookFlatDtoWithCount<AuthorBookFlatDto>(List<AuthorBookFlatDto> content, Long total) {}
