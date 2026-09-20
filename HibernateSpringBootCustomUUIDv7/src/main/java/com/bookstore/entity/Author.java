package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import com.bookstore.generator.Uuidv7Generator;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    // @Uuidv7Generator // custom implementation (written by David Ankin) of UUID v7 
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7) // Hibernate implementation
    private UUID id;

    private String name;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
