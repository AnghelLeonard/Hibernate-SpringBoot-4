package com.bookstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityResult;
import jakarta.persistence.FieldResult;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SqlResultSetMapping;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Single entity
@NamedNativeQuery(
        name = "Author.fetchAll",
        query = "SELECT * FROM author",
        resultSetMapping = "AuthorMapping"
)
@SqlResultSetMapping(
        name = "AuthorMapping",
        entities = {
            @EntityResult(
                    entityClass = Author.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "name", column = "author_name"),
                        @FieldResult(name = "genre", column = "author_genre"),
                        @FieldResult(name = "age", column = "age")
                    }
            )
        }
)

// Multiple entities
@NamedNativeQuery(
        name = "Author.fetchWithBook",
        query = "SELECT a.*, b.* FROM author a INNER JOIN book b ON a.id = b.author_id",
        resultSetMapping = "AuthorWithBookMapping"
)
@SqlResultSetMapping(
        name = "AuthorWithBookMapping",
        entities = {
            @EntityResult(
                    entityClass = Author.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "name", column = "author_name"),
                        @FieldResult(name = "genre", column = "author_genre"),
                        @FieldResult(name = "age", column = "age")
                    }
            ),
            @EntityResult(
                    entityClass = Book.class,
                    fields = {
                        @FieldResult(name = "id", column = "id"),
                        @FieldResult(name = "title", column = "book_title"),
                        @FieldResult(name = "isbn", column = "book_isbn")
                    }
            )
        }
)
@Entity
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "author_name")
    private String name;

    @Column(name = "author_genre")
    private String genre;

    private int age;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "author", orphanRemoval = true)
    private List<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
    }

    public void removeBook(Book book) {
        book.setAuthor(null);
        this.books.remove(book);
    }

    public void removeBooks() {
        Iterator<Book> iterator = this.books.iterator();

        while (iterator.hasNext()) {
            Book book = iterator.next();

            book.setAuthor(null);
            iterator.remove();
        }
    }

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name
                + ", genre=" + genre + ", age=" + age + '}';
    }

}
