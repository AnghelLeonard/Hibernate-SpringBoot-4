package com.bookstore.spring.dto;

import java.util.List;

public interface AuthorSummary {

    public String getName();
    public String getGenre();
    public List<BookSummary> getBooks();

    interface BookSummary {
        public String getTitle();
    }
}
