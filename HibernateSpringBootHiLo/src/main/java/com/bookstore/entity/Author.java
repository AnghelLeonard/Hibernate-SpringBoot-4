package com.bookstore.entity;

import com.bookstore.generator.AuthorId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id     
    @AuthorId
    // @AuthorId(sequenceName="five_sequence", initialValue=5, incrementSize=5)
    private Long id;
    
    /* you can use this with "hi/lo" if you un-comment it in application.properties
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "author_seq_gen")
    @SequenceGenerator(
        name = "author_seq_gen", 
        sequenceName = "author_seq", 
        allocationSize = 50 
    )
    private Long id;
    */

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
