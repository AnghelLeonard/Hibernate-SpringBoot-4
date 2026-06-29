package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {
 
    @Column(name = "deleted")
    protected boolean deleted;     
}
