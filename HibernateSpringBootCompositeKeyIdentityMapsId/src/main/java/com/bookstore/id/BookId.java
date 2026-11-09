package com.bookstore.id;

import java.io.Serializable;
import java.util.Objects;

public class BookId implements Serializable {

    private Long publisher;
    private Long bookNumber;

    public BookId() {
    }

    public BookId(Long publisher, Long bookNumber) {
        this.publisher = publisher;
        this.bookNumber = bookNumber;
    }

    public Long getPublisher() {
        return publisher;
    }

    public void setPublisher(Long publisher) {
        this.publisher = publisher;
    }

    public Long getBookNumber() {
        return bookNumber;
    }

    public void setBookNumber(Long bookNumber) {
        this.bookNumber = bookNumber;
    }

    @Override
    public boolean equals(Object o) {
        
        if (this == o) {
            return true;
        }
        
        if (!(o instanceof BookId bookId)) {
            return false;
        }
        
        return Objects.equals(publisher, bookId.publisher)
                && Objects.equals(bookNumber, bookId.bookNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisher, bookNumber);
    }
}
