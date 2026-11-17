package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.domain.Persistable;

/*
Trap 4 (author access): When iterating book.getBookAuthors() and calling ba.getAuthor(), 
       Hibernate fires an individual SELECT for each author row—and immediately 
       triggers Trap 5 inside Author.
 */
@Entity
public class BookAuthor implements Persistable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "royalty_split", nullable = false)
    private BigDecimal royaltySplit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)    
    private Book book;

    // TRAP 4: Lazy ManyToOne resolved inside a nested stream loop
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id", nullable = false)
    private Author author;

    @Transient
    private boolean isNew = true;

    protected BookAuthor() {}

    public BookAuthor(Long id, String role, BigDecimal royaltySplit, Book book, Author author) {
        this.id = id;
        this.role = role;
        this.royaltySplit = royaltySplit;
        this.book = book;
        this.author = author;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public BigDecimal getRoyaltySplit() {
        return royaltySplit;
    }

    public void setRoyaltySplit(BigDecimal royaltySplit) {
        this.royaltySplit = royaltySplit;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }   

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Book)) {
            return false;
        }

        BookAuthor that = (BookAuthor) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
       
        return getClass().hashCode();
    }
}
