package com.bookstore.entity;

import com.bookstore.generator.AuthorId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    // This will disable insert batching - AVOID IT!
    // @GeneratedValue(strategy = GenerationType.IDENTITY)

    // This will work, but better use the below solution to reduce database roundtrips
    // @GeneratedValue(strategy = GenerationType.AUTO)
    
    // This will allow insert batching and optimizes the identifiers
    // generation via the hi/lo algorithm which generated in-memory identifiers      
    @AuthorId    
    private Long id;

    private int age;
    private String name;
    private String genre;

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

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", age=" + age
                + ", name=" + name + ", genre=" + genre + '}';
    }
}
