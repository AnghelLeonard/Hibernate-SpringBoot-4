package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import org.hibernate.annotations.Generated;
import static org.hibernate.generator.EventType.INSERT;
import static org.hibernate.generator.EventType.UPDATE;

@Entity
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    protected Long id;

    private String title;
    private String isbn;
    private Double price;

    @Generated(event = {INSERT, UPDATE})
    @Column(insertable = false, updatable = false /* no longer recommended -> , 
                                                  columnDefinition = "DOUBLE AS (price - price * 0.25)" */ )
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

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", isbn="
                + isbn + ", price=" + price + ", discounted=" + discounted + '}';
    }
}
