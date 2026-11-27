package com.bookstore.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String title;
    private String isbn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    public Long getId() {
        return id;
    }

    public Book withId(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Book withTitle(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public Book withIsbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Author getAuthor() {
        return author;
    }

    public Book withAuthor(Author author) {
        this.author = author;
        return this;
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
        
        return id != null && id.equals(((Book) obj).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", isbn=" + isbn + '}';
    }

}
