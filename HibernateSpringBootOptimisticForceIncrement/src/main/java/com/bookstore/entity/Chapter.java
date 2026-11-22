package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

@Entity
public class Chapter implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String title;
    private String content;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVersion() {
        return version;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }    

    @Override
    public String toString() {
        return "Chapter{" + "id=" + id + ", title=" + title + ", content=" + content 
                + ", version=" + version + '}';
    }        
}
