package com.bookstore.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.springframework.data.domain.Persistable;

@Entity
public class Book implements Persistable<Long>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @Column(name = "status", nullable = false)
    private String status;

    // FIX Trap 1: Explicitly set FetchType.LAZY
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")    
    private Publisher publisher;

    // FIX Trap 2: Keep LAZY, and always join-fetch it in queries that need it
    // (Alternative structural fix: remove this bidirectional field or use @MapsId)
    @OneToOne(mappedBy = "book", fetch = FetchType.LAZY)
    private RoyaltyContract royaltyContract;

    // FIX Trap 3: Remains LAZY by default
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "book", orphanRemoval = true)
    private List<BookAuthor> bookAuthors;

    @Transient
    private boolean isNew = true;

    protected Book() {}

    public Book(Long id, String isbn, String status, Publisher publisher) {
        this.id = id;
        this.isbn = isbn;
        this.status = status;
        this.publisher = publisher;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public void addBookAuthor(BookAuthor bookAuthor) {
        this.bookAuthors.add(bookAuthor);
        bookAuthor.setBook(this);
    }

    public void removeBookAuthor(BookAuthor bookAuthor) {
        bookAuthor.setAuthor(null);
        this.bookAuthors.remove(bookAuthor);
    }

    public void removeBookAuthors() {
        Iterator<BookAuthor> iterator = this.bookAuthors.iterator();

        while (iterator.hasNext()) {
            BookAuthor bookAuthor = iterator.next();

            bookAuthor.setAuthor(null);
            iterator.remove();
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public RoyaltyContract getRoyaltyContract() {
        return royaltyContract;
    }

    public void setRoyaltyContract(RoyaltyContract royaltyContract) {
        this.royaltyContract = royaltyContract;
    }

    public List<BookAuthor> getBookAuthors() {
        return bookAuthors;
    }

    public void setBookAuthors(List<BookAuthor> bookAuthors) {
        this.bookAuthors = bookAuthors;
    }   
}
