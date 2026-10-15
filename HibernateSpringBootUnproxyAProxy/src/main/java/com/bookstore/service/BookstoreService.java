package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import com.bookstore.entity.Author;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private Author author;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }

    public void authorNotEqualsProxy() {

        // behind findById() we have EntityManager#find()        
        author = authorRepository.findById(1L).orElseThrow();

        // behind getReferenceById() we have EntityManager#getReference()        
        Author proxy = authorRepository.getReferenceById(1L);

        boolean nameIsInitialized = Hibernate.isPropertyInitialized(proxy, "name");
        boolean isEntityLoaded = Hibernate.isInitialized(proxy);

        System.out.println("Author class: " + author.getClass().getName());
        System.out.println("Proxy class: " + proxy.getClass().getName());
        System.out.println("'author' equals 'proxy'? " + author.equals(proxy));
        System.out.println("The 'name' property was initialized: " + nameIsInitialized);
        System.out.println("The 'proxy' was initialized: " + isEntityLoaded);
    }

    @Transactional(readOnly = true)
    public void authorEqualsUnproxy() {
        
        // behind getReferenceById() we have EntityManager#getReference()        
        Author proxy = authorRepository.getReferenceById(1L);
        
        Object unproxy = Hibernate.unproxy(proxy);
        
        boolean nameIsInitialized = Hibernate.isPropertyInitialized(proxy, "name");
        boolean isEntityLoaded = Hibernate.isInitialized(proxy);
       
        System.out.println("Author class: " + author.getClass().getName());
        System.out.println("Unproxy class: " + unproxy.getClass().getName());
        System.out.println("'author' equals 'unproxy'? " + author.equals(unproxy)); 
        System.out.println("The 'name' property was initialized: " + nameIsInitialized);
        System.out.println("The 'proxy' was initialized: " + isEntityLoaded);
    }
}
