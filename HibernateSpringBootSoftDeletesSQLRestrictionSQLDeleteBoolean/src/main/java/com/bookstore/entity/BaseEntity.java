package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
 
    @Column(name = "deleted", nullable = false)
    protected boolean deleted;  

    public boolean isDeleted() {
        return deleted;
    }     
}
