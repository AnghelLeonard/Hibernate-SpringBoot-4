package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import java.io.Serializable;

@Entity
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "CountByGenreProcedure",
            procedureName = "COUNT_AUTHOR_BY_GENRE",
            resultClasses = {Author.class},
            parameters = {
                @StoredProcedureParameter(
                        name = "p_genre",
                        type = String.class,
                        mode = ParameterMode.IN),
                @StoredProcedureParameter(
                        name = "p_count",
                        type = Integer.class,
                        mode = ParameterMode.OUT)})
})
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
