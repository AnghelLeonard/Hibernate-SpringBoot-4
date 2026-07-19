package com.bookstore.forum.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @Tsid
    private Long id;

    @NaturalId
    @Column(unique = true)
    private String name;

    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
