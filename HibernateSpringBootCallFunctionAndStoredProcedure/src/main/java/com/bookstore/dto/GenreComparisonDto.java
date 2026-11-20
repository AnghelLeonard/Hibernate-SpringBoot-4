package com.bookstore.dto;

import java.util.List;

public record GenreComparisonDto(
    List<NicknameAndAgeDto> matchingGenre,
    List<GenreAndAgeDto> otherGenres
) {}
