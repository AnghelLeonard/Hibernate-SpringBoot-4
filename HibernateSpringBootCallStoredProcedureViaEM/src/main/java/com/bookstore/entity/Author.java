package com.bookstore.entity;

import com.bookstore.dto.AuthorDto;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQueries;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.StoredProcedureParameter;
import java.io.Serializable;

@Entity
@NamedStoredProcedureQueries({
    @NamedStoredProcedureQuery(
            name = "FetchByGenreProcedure",
            procedureName = "FETCH_AUTHOR_BY_GENRE",
            resultClasses = {Author.class},
            parameters = {
                @StoredProcedureParameter(
                        name = "p_genre",
                        type = String.class,
                        mode = ParameterMode.IN)})
})
@SqlResultSetMapping(name = "AuthorDtoMapping",
        classes = @ConstructorResult(targetClass = AuthorDto.class,
                columns = {
                    @ColumnResult(name = "nickname"),
                    @ColumnResult(name = "age")}))
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int age;
    private String name;
    private String genre;
    private String nickname;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", age=" + age + ", name=" + name
                + ", genre=" + genre + ", nickname=" + nickname + '}';
    }
}
