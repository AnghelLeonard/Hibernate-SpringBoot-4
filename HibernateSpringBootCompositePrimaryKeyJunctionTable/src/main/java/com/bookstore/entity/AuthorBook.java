package com.bookstore.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Entity
public class AuthorBook implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EmbeddedId
    private AuthorBookId id;
    
    @MapsId("authorId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Author author;
    
    @MapsId("bookId")
    @ManyToOne(fetch = FetchType.LAZY)
    private Book book;
    
    private Instant publishedOn = Instant.now();

    public AuthorBook() {
    }

    public AuthorBook(Author author, Book book) {
        this.author = author;
        this.book = book;
        this.id = new AuthorBookId(author.getId(), book.getId());
    }

    public AuthorBookId getId() {
        return id;
    }

    public void setId(AuthorBookId id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Instant getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Instant publishedOn) {
        this.publishedOn = publishedOn;
    }
        
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.author);
        hash = 29 * hash + Objects.hashCode(this.book);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuthorBook other = (AuthorBook) obj;
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        return Objects.equals(this.book, other.book);
    }
}
