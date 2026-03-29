package com.bookstore.service;

import com.bookstore.jdbcTemplate.dto.AuthorExtractor;
import com.bookstore.spring.dto.AuthorDto;
import com.bookstore.spring.dto.SimpleAuthorDto;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.transform.dto.AuthorTransformer;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final AuthorTransformer authorTransformer;
    private final AuthorExtractor authorExtractor;

    public BookstoreService(AuthorRepository authorRepository,
            AuthorTransformer authorTransformer,
            AuthorExtractor authorExtractor,
            EntityManager entityManager) {

        this.authorRepository = authorRepository;
        this.authorTransformer = authorTransformer;
        this.authorExtractor = authorExtractor;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> fetchAuthorsWithBooksQueryBuilderMechanism() {
        List<AuthorDto> authors = authorRepository.findBy();

        System.out.println("\nResult set:" + authors.size());
        authors.forEach(a -> System.out.println("Books: " + a.getBooks().size()));

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorDto> fetchAuthorsWithBooksViaQuery() {
        List<AuthorDto> authors = authorRepository.findByViaQuery();

        System.out.println("\nResult set:" + authors.size());
        authors.forEach(a -> System.out.println("Books: " + a.getBooks().size()));

        return authors;
    }

    @Transactional(readOnly = true)
    public Set<AuthorDto> fetchAuthorsWithBooksViaJoinFetch() {
        Set<AuthorDto> authors = authorRepository.findByJoinFetch();

        System.out.println("\nResult set:" + authors.size());
        authors.forEach(a -> System.out.println("Books: " + a.getBooks().size()));

        return authors;
    }

    @Transactional(readOnly = true)
    public List<SimpleAuthorDto> fetchAuthorsWithBooksViaQuerySimpleDto() {
        List<SimpleAuthorDto> authors = authorRepository.findByViaQuerySimpleDto();

        System.out.println("\nResult set:" + authors.size());        

        return authors;
    }

    @Transactional(readOnly = true)
    public List<Object[]> fetchAuthorsWithBooksViaArrayOfObjects() {
        List<Object[]> authors = authorRepository.findByViaArrayOfObjects();

        System.out.println("\nResult set:" + authors.size());        

        return authors;
    }

    @Transactional(readOnly = true)
    public List<com.bookstore.transform.dto.AuthorDto> fetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto() {

        List<Object[]> authors = authorRepository.findByViaArrayOfObjectsWithIds();
        List<com.bookstore.transform.dto.AuthorDto> authorsDto = authorTransformer.transform(authors);

        System.out.println("\nResult set:" + authors.size());
        authorsDto.forEach(a -> System.out.println("Books: " + a.getBooks().size()));

        return authorsDto;
    }

    @Transactional(readOnly = true)
    public List<com.bookstore.jdbcTemplate.dto.AuthorDto> fetchAuthorsWithBooksViaJdbcTemplateToDto() {

        List<com.bookstore.jdbcTemplate.dto.AuthorDto> authors = authorExtractor.extract();

        System.out.println("\nResult set:" + authors.size());
        authors.forEach(a -> System.out.println("Books: " + a.getBooks().size()));

        return authors;
    }
}
