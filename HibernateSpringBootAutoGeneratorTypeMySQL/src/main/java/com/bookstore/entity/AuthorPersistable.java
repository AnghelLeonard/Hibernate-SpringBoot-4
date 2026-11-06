package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

@Entity
public class AuthorPersistable implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id         
    private Long id;

    private Integer age;
    private String name;
    private String genre;
    
    // create a hidden flag that tells Spring if this entity is new
    @Transient
    private boolean isNew = true; 

    // override the method so Spring checks the flag instead of the ID field
    @Override
    public boolean isNew() {
        return isNew;
    }

    @Override
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
    
    // flip the flag to false after Hibernate loads or saves it
    @PostLoad
    @PostPersist
    public void markNotNew() {
        this.isNew = false;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", age=" + age
                + ", name=" + name + ", genre=" + genre + '}';
    }
}
