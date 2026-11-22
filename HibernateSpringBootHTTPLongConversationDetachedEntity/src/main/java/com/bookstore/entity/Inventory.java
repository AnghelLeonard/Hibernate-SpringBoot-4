package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String title;

    @Min(value = 0)
    @Max(value = 100)
    private int quantity;
    
    @Version
    private Integer version;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Inventory{" + "id=" + id + ", title=" + title 
                + ", quantity=" + quantity + ", version=" + version + '}';
    }   
}
