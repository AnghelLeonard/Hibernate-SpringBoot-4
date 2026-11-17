package com.bookstore.dto;

import java.util.List;

public record BookCatalogDto(
        String isbn,
        String publisherName,
        String contractCode,
        List<AuthorContributionDto> authors
) {}
