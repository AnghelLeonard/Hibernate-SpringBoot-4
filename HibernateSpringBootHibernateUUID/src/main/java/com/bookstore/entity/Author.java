package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;

@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id   
    //@UuidGenerator                                         // UUIDv4
    //@UuidGenerator(style = UuidGenerator.Style.AUTO)       // UUIDv4
    //@UuidGenerator(style = UuidGenerator.Style.RANDOM)     // UUIDv4
    //@UuidGenerator(style = UuidGenerator.Style.TIME)       // UUIDv1
    //@UuidGenerator(style = UuidGenerator.Style.VERSION_6)  // UUIDv6
    @UuidGenerator(style = UuidGenerator.Style.VERSION_7)    // UUIDv7
    
    // this is an alternative of using the JPA's @GeneratedValue for UUIDv4
    // @GeneratedValue(generator = "org.hibernate.id.uuid.UuidGenerator") 
    private UUID id;       
    
    private Integer age;
    private String name;
    private String genre;

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

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", age=" + age
                + ", name=" + name + ", genre=" + genre + '}';
    }
}
