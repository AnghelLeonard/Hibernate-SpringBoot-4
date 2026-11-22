package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

@Entity
public class Modification implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;
    
    private String description;
    private String modification;
    
    @ManyToOne(fetch = FetchType.LAZY)  
    private Chapter chapter;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
        
    public String getModification() {
        return modification;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }  

    @Override
    public String toString() {
        return "Modification{" + "id=" + id + ", description=" + description 
                + ", modification=" + modification + '}';
    }        
}
