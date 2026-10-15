package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }

    @Transactional
    public void inspectCollectionProxy() {
        // fetch the author (not the books)
        Author author = authorRepository.findById(1L).orElseThrow();

        // check the concrete class of the collection in memory
        System.out.println("Collection class type before access: " 
            + author.getBooks().getClass().getName());        

        // verify if the actual elements inside the proxy have been fetched
        boolean isLoadedBefore = Hibernate.isInitialized(author.getBooks());
        System.out.println("Are the books loaded into memory? " + isLoadedBefore);
        
        // unproxy - the moment you call a method that inherently needs the actual child data
        // (e.g., .size() or .iterator())
        int totalBooks = author.getBooks().size(); 
        // 💥 HIBERNATE INTERCEPTS THIS AND TRIGGER A SELECT        

        // Check the state of the proxy again
        boolean isLoadedAfter = Hibernate.isInitialized(author.getBooks());
        System.out.println("Are the books loaded after access? " + isLoadedAfter);        
    }
}
