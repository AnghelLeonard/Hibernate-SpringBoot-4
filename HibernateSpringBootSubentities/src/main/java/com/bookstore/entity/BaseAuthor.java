package com.bookstore.entity;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class BaseAuthor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    private Integer age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "BaseAuthor{" + "id=" + id + ", age=" + age 
                + ", name=" + name + ", genre=" + genre + '}';
    }   
}
