package com.bookstore.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class BaseEntity<U> {        

    @Id    
    protected Long id;
    
    @CreatedDate
    protected Instant created;
 
    @CreatedBy
    protected U createdBy;
    
    @LastModifiedDate
    protected Instant lastModified;          
 
    @LastModifiedBy
    protected U lastModifiedBy;
}
