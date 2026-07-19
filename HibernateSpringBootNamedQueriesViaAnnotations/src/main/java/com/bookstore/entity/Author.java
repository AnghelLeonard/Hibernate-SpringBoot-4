package com.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.io.Serializable;

@NamedQueries({
    @NamedQuery(name = "Author.fetchAll",
            query = "SELECT a FROM Author a"),

    @NamedQuery(name = "Author.fetchByNameAndAge",
            query = "SELECT a FROM Author a WHERE a.name=?1 AND a.age=?2"),
     
    @NamedQuery(name = "Author.fetchPageSort",
            query = "SELECT a FROM Author a"), // sort is ignored

    @NamedQuery(name = "Author.fetchPageSortWhere",
            query = "SELECT a FROM Author a WHERE a.age > ?1"), // sort is ignored

    @NamedQuery(name = "Author.fetchSliceSort",
            query = "SELECT a FROM Author a"), // sort is ignored

    @NamedQuery(name = "Author.fetchSliceSortWhere",
            query = "SELECT a FROM Author a WHERE a.age > ?1") // sort is ignored
})

@NamedNativeQueries({
    @NamedNativeQuery(name = "Author.fetchAllNative",
            query = "SELECT * FROM author",
            resultClass = Author.class),

    @NamedNativeQuery(name = "Author.fetchByNameAndAgeNative",
            query = "SELECT * FROM author WHERE name=?1 AND age=?2",
            resultClass = Author.class),
   
    @NamedNativeQuery(name = "Author.fetchPageSortNative",
            query = "SELECT * FROM Author a",
            resultClass = Author.class), // sort is ignored

    @NamedNativeQuery(name = "Author.fetchPageSortWhereNative",
            query = "SELECT * FROM Author a WHERE a.age > ?1",
            resultClass = Author.class), // sort is ignored

    @NamedNativeQuery(name = "Author.fetchSliceSortNative",
            query = "SELECT * FROM author",
            resultClass = Author.class), // sort is ignored

    @NamedNativeQuery(name = "Author.fetchSliceSortWhereNative",
            query = "SELECT * FROM author WHERE age > ?1",
            resultClass = Author.class) // sort is ignored
})
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
