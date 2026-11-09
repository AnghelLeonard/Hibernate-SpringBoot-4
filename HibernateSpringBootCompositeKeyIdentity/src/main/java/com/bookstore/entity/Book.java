package com.bookstore.entity;

import com.bookstore.id.BookId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import java.io.Serializable;

@Entity
@IdClass(BookId.class)
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "publisher_id")
    private Long publisherId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_number")
    private Long bookNumber;

    @Column(name = "title", nullable = false)
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" + "publisherId=" + publisherId
                + ", bookNumber=" + bookNumber + ", title=" + title + '}';
    }
}
