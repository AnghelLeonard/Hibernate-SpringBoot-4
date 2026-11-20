package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedStoredProcedureQuery;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureParameter;
import java.io.Serializable;

@Entity
@NamedStoredProcedureQuery(
        name = "Author.fetchNicknameAndAgeById",
        procedureName = "fetch_nickname_and_age_by_id",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_id", type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "o_nickname", type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT, name = "o_age", type = Integer.class)
        }
)

@NamedStoredProcedureQuery(
    name = "Author.fetchNicknameAndAgeByGenre",
    procedureName = "fetch_nickname_and_age_by_genre",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_genre", type = String.class),        
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor", type = void.class)
    }
)

@NamedStoredProcedureQuery(
    name = "Author.fetchNicknameGenreAndAgeByGenre",
    procedureName = "fetch_nickname_genre_and_age_by_genre",
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_genre", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor_1", type = void.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor_2", type = void.class)
    }
)

@NamedStoredProcedureQuery(
    name = "Author.fetchAuthorByGenre",
    procedureName = "fetch_author_by_genre",
    resultClasses = Author.class, // Maps SELECT * directly to Author entities
    parameters = {
        @StoredProcedureParameter(mode = ParameterMode.IN, name = "p_genre", type = String.class),
        @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, name = "p_cursor", type = void.class)
    }
)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private Integer age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
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
