package com.bookstore.entity;

import com.bookstore.dto.AuthorDto;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import java.io.Serializable;

// Scalar Mapping
@SqlResultSetMapping(
        name = "AuthorsNameMapping",
        columns = {
            @ColumnResult(name = "name")
        }
)
@NamedNativeQuery(
        name = "AuthorsNameQuery",
        query = "SELECT name FROM author",
        resultSetMapping = "AuthorsNameMapping"
)

// Constructor Mapping
@NamedNativeQuery(
        name = "AuthorDtoQuery",
        query = "SELECT name, age FROM author",
        resultSetMapping = "AuthorDtoMapping"
)
@SqlResultSetMapping(
        name = "AuthorDtoMapping",
        classes = @ConstructorResult(
                targetClass = AuthorDto.class,
                columns = {
                    @ColumnResult(name = "name"),
                    @ColumnResult(name = "age")
                }
        )
)
@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private String name;
    private String genre;

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
