package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

/*
Trap 5 (deep @ManyToOne LAZY proxy): Explicitly set to FetchType.LAZY, so initializing an 
        Author leaves agency as an uninitialized proxy. The moment your DTO mapper calls
        author.getAgency().getName(), Hibernate fires a separate SELECT to fetch "literary_agency".
 */
@Entity
public class Author implements Persistable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "pen_name", nullable = false)
    private String penName;

    // TRAP 5: Inside the DTO mapper by calling author.getAgency().getName()
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id", referencedColumnName = "id")
    private LiteraryAgency agency;

    @Transient
    private boolean isNew = true;

    protected Author() {}

    public Author(Long id, String penName, LiteraryAgency agency) {
        this.id = id;
        this.penName = penName;
        this.agency = agency;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public String getPenName() {
        return penName;
    }

    public void setPenName(String penName) {
        this.penName = penName;
    }

    public LiteraryAgency getAgency() {
        return agency;
    }

    public void setAgency(LiteraryAgency agency) {
        this.agency = agency;
    }  
}
