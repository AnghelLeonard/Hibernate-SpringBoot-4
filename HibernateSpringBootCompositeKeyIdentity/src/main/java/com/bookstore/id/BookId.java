package com.bookstore.id;

import java.io.Serializable;
import java.util.Objects;

public class BookId implements Serializable {

    private Long publisherId;
    private Long bookNumber;

    public BookId() {
    }

    public BookId(Long publisherId, Long bookNumber) {
        this.publisherId = publisherId;
        this.bookNumber = bookNumber;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Long publisherId) {
        this.publisherId = publisherId;
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
        
        return Objects.equals(publisherId, bookId.publisherId)
                && Objects.equals(bookNumber, bookId.bookNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherId, bookNumber);
    }
}
