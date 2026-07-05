package com.bookstore.dto;

import java.io.Serializable;

public class AuthorDtoC implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String name;

    public AuthorDtoC(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "AuthorDtoC{" + "name=" + name + ", id=" + id + '}';
    }
}
