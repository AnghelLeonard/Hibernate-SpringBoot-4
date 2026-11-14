package com.bookstore.entity;

import com.bookstore.by.CreatedBy;
import com.bookstore.by.ModifiedBy;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@MappedSuperclass
public abstract class BaseEntity<U> {

    @Id    
    protected Long id;

    @CreationTimestamp
    protected Instant created;

    @UpdateTimestamp
    protected Instant lastModified;

    @CreatedBy
    protected U createdBy;
    
    @ModifiedBy
    protected U lastModifiedBy;
}
