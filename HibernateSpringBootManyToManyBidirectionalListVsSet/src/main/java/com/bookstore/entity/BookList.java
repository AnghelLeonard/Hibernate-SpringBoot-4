package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class BookList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String title;
    private String isbn;

    @ManyToMany(mappedBy = "books")
    private List<AuthorList> authors = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public List<AuthorList> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorList> authors) {
        this.authors = authors;
    }

    @Override
    public boolean equals(Object obj) {        

        if (this == obj) {
            return true;
        }
        
        if (!(obj instanceof BookList)) {
            return false;
        }

        return id != null && id.equals(((BookList) obj).id);
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
