package com.bookstore.entity;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class AuthorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;
    
    @Column(name = "age")
    private Integer age;

    public AuthorId() {
    }

    public AuthorId(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + this.age;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        final AuthorId other = (AuthorId) obj;
        if (!Objects.equals(this.age, other.age)) {
            return false;
        }
        
        return Objects.equals(this.name, other.name);
    }        

    @Override
    public String toString() {
        return "AuthorId{" + "name=" + name + ", age=" + age + '}';
    }        
}
