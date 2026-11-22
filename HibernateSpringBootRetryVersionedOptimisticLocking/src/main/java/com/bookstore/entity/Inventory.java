package com.bookstore.entity;

import com.bookstore.exception.OutOfStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import java.io.Serializable;

@Entity
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    
    private Integer availableQuantity;   
    private String sku;

    @Version
    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void reserve(int quantity) {
        
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        
        if (this.availableQuantity < quantity) {
            throw new OutOfStockException(
                "Insufficient stock for SKU " + sku + ". Requested: " + quantity + ", Available: " + availableQuantity
            );
        }
        
        this.availableQuantity -= quantity;
    }

    public Integer getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Inventory{" + "id=" + id + ", availableQuantity=" + availableQuantity 
                + ", sku=" + sku + ", version=" + version + '}';
    }        
}
