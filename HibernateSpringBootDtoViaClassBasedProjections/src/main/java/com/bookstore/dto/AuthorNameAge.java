package com.bookstore.dto;

import java.util.Objects;

// or, as a Java record
// public record AuthorNameAge(Integer age, String name) {}

public class AuthorNameAge {
    
    private String name;
    private Integer age;

    public AuthorNameAge(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.name);
        hash = 67 * hash + Objects.hashCode(this.age);
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
        
        final AuthorNameAge other = (AuthorNameAge) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        
        return Objects.equals(this.age, other.age);
    }    
}
