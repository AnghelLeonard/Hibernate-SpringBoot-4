package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String name;
    private String genre;
    private Integer age;
    private Integer sellrank;
    private Integer royalties;
    private Integer rating;

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

    public Integer getSellrank() {
        return sellrank;
    }

    public void setSellrank(Integer sellrank) {
        this.sellrank = sellrank;
    }

    public Integer getRoyalties() {
        return royalties;
    }

    public void setRoyalties(Integer royalties) {
        this.royalties = royalties;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name + ", genre=" + genre 
                + ", age=" + age + ", sellrank=" + sellrank + ", royalties=" + royalties 
                + ", rating=" + rating + '}';
    }    
}
