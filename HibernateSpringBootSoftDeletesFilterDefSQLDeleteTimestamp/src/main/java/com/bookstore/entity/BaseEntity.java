package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.hibernate.annotations.FilterDef;

@MappedSuperclass
@FilterDef(name = "excludeDeleted", autoEnabled = true)
public abstract class BaseEntity {
 
    @Column(name = "deleted")
    protected Instant deleted;     
}
