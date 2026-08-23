package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonView;

public record AuthorDto(
        @JsonView(Views.NameEmail.class)
        String name,
        @JsonView(Views.NameEmail.class)
        String email,
        @JsonView(Views.NameEmailAgeGenre.class)
        Integer age,
        @JsonView(Views.NameEmailAgeGenre.class)
        String genre,
        @JsonView(Views.All.class)
        String address
        ) {}
