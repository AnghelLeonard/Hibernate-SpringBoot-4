package com.bookstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String name;
    private String genre;
    private Integer age;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "author", orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public Author addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
        return this;
    }

    public Author removeBook(Book book) {
        book.setAuthor(null);
        this.books.remove(book);
        return this;
    }

    public Long getId() {
        return id;
    }
    
    public Author withId(Long id) {
        this.id = id;
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Author withName(String name) {
        this.name = name;
        return this;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public Author withGenre(String genre) {
        this.genre = genre;
        return this;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAge() {
        return age;
    }

    public Author withAge(Integer age) {
        this.age = age;
        return this;
    }
    
    public void setAge(int age) {
        this.age = age;
    }

    public List<Book> getBooks() {
        return books;
    }
    
    public Author withBooks(List<Book> books) {
        this.books = books;
        return this;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name
                + ", genre=" + genre + ", age=" + age + '}';
    }

}
