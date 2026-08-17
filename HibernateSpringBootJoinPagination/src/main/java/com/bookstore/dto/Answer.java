package com.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonView;

public record Answer (
        @JsonView(Views.Summary.class)
        String name,
        @JsonView(Views.Summary.class)
        int age,
        @JsonView(Views.Summary.class)
        String title, 
        @JsonView(Views.Summary.class)
        String isbn//,
       // @JsonView(Views.Summary.class)
       // long total
        ){}
