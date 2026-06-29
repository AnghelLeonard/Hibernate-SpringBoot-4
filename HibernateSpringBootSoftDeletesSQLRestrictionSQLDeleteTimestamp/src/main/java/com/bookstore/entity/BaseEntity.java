package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
 
    @Column(name = "deleted")
    protected LocalDateTime deleted;     
}
