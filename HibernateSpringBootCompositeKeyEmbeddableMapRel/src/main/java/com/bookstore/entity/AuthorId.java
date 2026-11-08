package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Embeddable
public class AuthorId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "publisher")
    @OnDelete(action = OnDeleteAction.CASCADE) // this is not used, but it can be useful
                                               // if you also want deleting a Publisher to cascade-delete Author rows
    private Publisher publisher;

    @Column(name = "name")
    private String name;

    public AuthorId() {
    }

    public AuthorId(Publisher publisher, String name) {
        this.publisher = publisher;
        this.name = name;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.publisher);
        hash = 97 * hash + Objects.hashCode(this.name);
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
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        
        return Objects.equals(this.publisher, other.publisher);
    }

    @Override
    public String toString() {
        return "AuthorId{ " + "publisher=" + publisher + ", name=" + name + '}';
    }   
}
