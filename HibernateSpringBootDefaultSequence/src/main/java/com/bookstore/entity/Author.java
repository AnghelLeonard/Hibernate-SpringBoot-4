package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import java.io.Serializable;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    // @GeneratedValue(strategy = GenerationType.SEQUENCE) // defaults to the "pooled" strategy
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_gen") // like default, but safer across frameworks
    @SequenceGenerator(
            name = "author_seq_gen",
            sequenceName = "author_seq",
            allocationSize = 50
    )
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
