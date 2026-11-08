package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "publisher")
    private Long publisherId;

    @Column(name = "name")
    private String name;

    public AuthorId() {
    }

    public AuthorId(Long publisherId, String name) {
        this.publisherId = publisherId;
        this.name = name;
    }

    public Long getPublisherId() {
        return publisherId;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.publisherId);
        hash = 97 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final AuthorId other = (AuthorId) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.publisherId, other.publisherId);
    }

    @Override
    public String toString() {
        return "AuthorId{publisherId=" + publisherId + ", name=" + name + '}';
    }
}
