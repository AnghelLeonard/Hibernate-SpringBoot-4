package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {
 
    @Column(name = "deleted")
    protected Instant deleted;     

    public Instant getDeleted() {
        return deleted;
    }        
}
