package com.bookstore.dto;

import java.util.List;

public record AuthorGenreResponse(
            List<NicknameAndAgeDto> matchingGenre,
            List<GenreAndAgeDto> otherGenres
    ) {}
