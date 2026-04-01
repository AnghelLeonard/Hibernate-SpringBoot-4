package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public BookstoreService(AuthorRepository authorRepository,
            EntityManager entityManager) {

        this.authorRepository = authorRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsReadOnlyEntities() {
        List<Author> authors = authorRepository.findAll();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsArrayOfObject() {
        List<Object[]> authors = authorRepository.fetchAsArray();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsArrayOfObjectColumns() {
        List<Object[]> authors = authorRepository.fetchAsArrayColumns();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsArrayOfObjectNative() {
        List<Object[]> authors = authorRepository.fetchAsArrayNative();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsArrayOfObjectQueryBuilderMechanism() {
        List<Object[]> authors = authorRepository.findFirstBy();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsDtoClass() {
        List<AuthorDto> authors = authorRepository.fetchAsDto();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsDtoClassColumns() {
        List<AuthorDto> authors = authorRepository.fetchAsDtoColumns();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsDtoClassNative() {
        List<AuthorDto> authors = authorRepository.fetchAsDtoNative();
        System.out.println("\nResult set:" + authors.size());    
    }

    @Transactional(readOnly = true)
    public void fetchAuthorAsDtoClassQueryBuilderMechanism() {
        List<AuthorDto> authors = authorRepository.findBy();
        System.out.println("\nResult set:" + authors.size());    
    }
}
