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

    // This will go for the SEQUENCE generator which uses the
    // pooled(hi/lo) algorithm which generated in-memory identifiers  
    // @GeneratedValue(strategy = GenerationType.AUTO)    
    //@GeneratedValue(strategy = GenerationType.SEQUENCE) // works as AUTO   
    
    // This will go for a custom hi/lo algorithm which uses an increment size of 100
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
