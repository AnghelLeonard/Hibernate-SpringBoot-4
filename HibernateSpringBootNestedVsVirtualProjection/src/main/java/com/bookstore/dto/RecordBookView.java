package com.bookstore.dto;

import com.bookstore.dto.view.Views;
import com.fasterxml.jackson.annotation.JsonView;

public record RecordBookView(
        @JsonView(Views.BookSummary.class) String title,
        @JsonView(Views.BookSummary.class) Integer rank,
        @JsonView(Views.BookDetailed.class) RecordAuthorView author
        ) {

    public record RecordAuthorView(
            @JsonView(Views.BookDetailed.class) String name, 
            @JsonView(Views.BookDetailed.class) String genre) {}
}
