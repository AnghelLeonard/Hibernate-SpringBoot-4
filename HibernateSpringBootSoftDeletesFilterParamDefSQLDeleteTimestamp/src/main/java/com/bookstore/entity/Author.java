package com.bookstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.SQLDelete;

@Entity
@SQLDelete(sql = "UPDATE author SET deleted = UTC_TIMESTAMP() WHERE id = ?")
@Filter(
        name = "includeExcludeDeleted",
        condition = """
                    (:restoreDeleted = true AND :includeDeleted = false AND :includeOnlyDeleted = false AND deleted IS NOT NULL)
                    OR (:restoreDeleted = false AND :includeDeleted = true AND :includeOnlyDeleted = false)
                    OR (:restoreDeleted = false AND :includeOnlyDeleted = true AND deleted IS NOT NULL)
                    OR (:restoreDeleted = false AND :includeDeleted = false AND :includeOnlyDeleted = false AND deleted IS NULL)
                    """
)
public class Author extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String genre;
    private int age;

    @Filter(
        name = "includeExcludeDeleted",
        condition = """
                    (:restoreDeleted = true AND :includeDeleted = false AND :includeOnlyDeleted = false AND deleted IS NOT NULL)
                    OR (:restoreDeleted = false AND :includeDeleted = true AND :includeOnlyDeleted = false)
                    OR (:restoreDeleted = false AND :includeOnlyDeleted = true AND deleted IS NOT NULL)
                    OR (:restoreDeleted = false AND :includeDeleted = false AND :includeOnlyDeleted = false AND deleted IS NULL)
                    """
    )
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

    public Instant getDeleted() {
        return deleted;
    }
    
    @PreRemove
    private void authorRemove() {
        deleted = null;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name
                + ", genre=" + genre + ", age=" + age + ", deleted =" + deleted + '}';
    }
}
