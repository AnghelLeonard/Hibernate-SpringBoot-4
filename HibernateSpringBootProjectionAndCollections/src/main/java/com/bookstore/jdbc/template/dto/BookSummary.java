package com.bookstore.jdbc.template.dto;

import java.io.Serializable;

public class BookSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookId;
    private String title;

    public BookSummary() {
    }

    public BookSummary(Long bookId, String title) {
        this.bookId = bookId;
        this.title = title;
    }

    public Long getId() {
        return bookId;
    }

    public void setId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BookDto{" + "bookId="
                + bookId + ", title=" + title + '}';
    }
}
