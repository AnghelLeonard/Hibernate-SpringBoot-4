package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import org.hibernate.annotations.JoinFormula;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;
    private int age;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinFormula("""
                 (
                   SELECT b.id FROM book b 
                   WHERE b.author_id = id 
                   ORDER BY b.price ASC LIMIT 1
                 )
                 """)
    private Book cheapestBook;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Book getCheapestBook() {
        return cheapestBook;
    }

    public void setCheapestBook(Book cheapestBook) {
        this.cheapestBook = cheapestBook;
    }    
    
    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name
                + ", genre=" + genre + ", age=" + age + '}';
    }

}
