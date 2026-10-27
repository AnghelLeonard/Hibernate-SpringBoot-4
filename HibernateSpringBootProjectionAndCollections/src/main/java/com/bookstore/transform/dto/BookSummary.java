package com.bookstore.transform.dto;

import java.io.Serializable;

// if you use a Java record then you'll have to modify AuthorTransformer accordingly
// public record BookSummary(Long bookId, String title) {}

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
