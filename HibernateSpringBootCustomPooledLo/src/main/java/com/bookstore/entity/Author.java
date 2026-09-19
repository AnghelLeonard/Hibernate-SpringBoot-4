package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import com.bookstore.generator.CustomId;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @CustomId(initialValue = 10, incrementSize = 25)
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
