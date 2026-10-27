package com.bookstore.service;

import com.bookstore.jdbc.template.dto.AuthorExtractor;
import com.bookstore.record.dto.FlatRecordAuthorBook;
import com.bookstore.record.dto.RecordAuthorSummary;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.transform.dto.AuthorTransformer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.spring.dto.AuthorSummary;
import com.bookstore.spring.dto.SimpleAuthorSummary;
import java.util.AbstractMap;
import java.util.stream.Collectors;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;
    private final AuthorTransformer authorTransformer;
    private final AuthorExtractor authorExtractor;

    @PersistenceContext
    private final EntityManager entityManager;

    public BookstoreService(AuthorRepository authorRepository,
            AuthorTransformer authorTransformer,
            AuthorExtractor authorExtractor,
            EntityManager entityManager) {

        this.authorRepository = authorRepository;
        this.entityManager = entityManager;
        this.authorTransformer = authorTransformer;
        this.authorExtractor = authorExtractor;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAuthorsWithBooksQueryBuilderMechanism() {
        List<AuthorSummary> authors = authorRepository.findBy();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println("\n\n" + a.getName() + ", " + a.getGenre());
            a.getBooks().forEach(b -> System.out.print(b.getTitle() + ", "));
        });

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAuthorsWithBooksViaQuery() {
        List<AuthorSummary> authors = authorRepository.findByViaQuery();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println("\n\n" + a.getName() + ", " + a.getGenre());
            a.getBooks().forEach(b -> System.out.print(b.getTitle() + ", "));
        });

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAuthorsWithBooksViaJoinFetch() {
        List<AuthorSummary> authors = authorRepository.findByJoinFetch();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println("\n\n" + a.getName() + ", " + a.getGenre());
            a.getBooks().forEach(b -> System.out.print(b.getTitle() + ", "));
        });

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<SimpleAuthorSummary> fetchAuthorsWithBooksViaQuerySimpleDto() {
        List<SimpleAuthorSummary> authors = authorRepository.findByViaQuerySimpleDto();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println(a.getName() + ", " + a.getGenre() + ", " + a.getTitle());
        });

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<RecordAuthorSummary> fetchAuthorsWithBooksViaQueryRecordDto() {
        List<FlatRecordAuthorBook> authors = authorRepository.findByViaQueryRecordDto();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println(a.name() + ", " + a.genre() + ", " + a.title());
        });

        briefOverviewOfPersistentContextContent();

        return authors.stream()
                .collect(Collectors.groupingBy(
                        flat -> new AbstractMap.SimpleEntry<>(flat.name(), flat.genre()),
                        Collectors.mapping(flat -> new RecordAuthorSummary.RecordBookSummary(
                        flat.title()), Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> new RecordAuthorSummary(
                entry.getKey().getKey(), // name
                entry.getKey().getValue(), // genre
                entry.getValue() // List<RecordBookSummary>
        )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Object[]> fetchAuthorsWithBooksViaArrayOfObjects() {
        List<Object[]> authors = authorRepository.findByViaArrayOfObjects();

        System.out.println("\nResult set:");
        authors.forEach(a -> System.out.println(Arrays.toString(a)));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<com.bookstore.transform.dto.AuthorSummary> fetchAuthorsWithBooksViaArrayOfObjectsAndTransformToDto() {

        List<Object[]> authors = authorRepository.findByViaArrayOfObjectsWithIds();
        List<com.bookstore.transform.dto.AuthorSummary> authorsSummary = authorTransformer.transform(authors);

        System.out.println("\nResult set:");
        authors.forEach(a -> System.out.println(Arrays.toString(a)));

        briefOverviewOfPersistentContextContent();

        return authorsSummary;
    }

    @Transactional(readOnly = true)
    public List<com.bookstore.jdbc.template.dto.AuthorSummary> fetchAuthorsWithBooksViaJdbcTemplateToDto() {

        List<com.bookstore.jdbc.template.dto.AuthorSummary> authors = authorExtractor.extract();

        System.out.println("\nResult set:");
        authors.forEach(a -> {
            System.out.println("\n\n" + a.getName() + ", " + a.getGenre());
            a.getBooks().forEach(b -> System.out.print(b.getTitle() + ", "));
        });

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    private void briefOverviewOfPersistentContextContent() {
        org.hibernate.engine.spi.PersistenceContext persistenceContext = getPersistenceContext();

        int managedEntities = persistenceContext.getNumberOfManagedEntities();
        int collectionEntriesSize = persistenceContext.getCollectionEntriesSize();

        System.out.println("\n-----------------------------------");
        System.out.println("Total number of managed entities: " + managedEntities);
        System.out.println("Total number of collection entries: " + collectionEntriesSize + "\n");

        // getEntitiesByKey() will be removed and probably replaced with #iterateEntities() 
        Map<EntityKey, Object> entitiesByKey = persistenceContext.getEntitiesByKey();
        entitiesByKey.forEach((key, value) -> System.out.println(key + ":" + value));

        for (Object entry : entitiesByKey.values()) {
            EntityEntry ee = persistenceContext.getEntry(entry);
            System.out.println(
                    "Entity name: " + ee.getEntityName()
                    + " | Status: " + ee.getStatus()
                    + " | State: " + Arrays.toString(ee.getLoadedState()));
        };

        System.out.println("\n-----------------------------------\n");
    }

    private org.hibernate.engine.spi.PersistenceContext getPersistenceContext() {

        SharedSessionContractImplementor sharedSession = entityManager.unwrap(
                SharedSessionContractImplementor.class
        );

        return sharedSession.getPersistenceContext();
    }

}
