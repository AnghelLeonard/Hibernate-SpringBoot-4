package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;
import java.io.Serializable;
 
@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    protected Long id;

    private String title;
    private String isbn;
    private Double price;

    @Transient
    private Double discounted;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscounted() {
        return discounted;
    }

    @PostLoad
    private void postLoad() {
        this.discounted = this.price - this.price * 0.25; 
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", isbn=" 
                + isbn + ", price=" + price + ", discounted=" + discounted + '}';
    }        
}
