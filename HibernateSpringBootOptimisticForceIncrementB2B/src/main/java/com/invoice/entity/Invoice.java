package com.invoice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.io.Serializable;
import java.math.BigDecimal;
import org.springframework.data.domain.Persistable;

@Entity
public class Invoice implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;
    
    private BigDecimal creditLimit;

    @Version
    private Integer version;
    
    @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }  

    public Integer getVersion() {
        return version;
    }       

    @Override
    public String toString() {
        return "Invoice{" + "id=" + id + ", creditLimit=" + creditLimit + ", version=" + version + '}';
    }        
}
