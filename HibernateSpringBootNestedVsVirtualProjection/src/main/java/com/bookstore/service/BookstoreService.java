package com.bookstore.service;

import java.util.Arrays;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.transaction.annotation.Transactional;
import com.bookstore.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.engine.spi.EntityKey;
import com.bookstore.dto.BookSummary;
import com.bookstore.dto.SimpleBookSummary;
import com.bookstore.dto.VirtualBookSummary;
import com.bookstore.dto.RecordBookSummary;
import com.bookstore.dto.RecordBookView;
import com.bookstore.dto.SimpleRecordBookSummary;

@Service
public class BookstoreService {

    private final BookRepository bookRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    public BookstoreService(BookRepository bookRepository, EntityManager entityManager) {
        this.bookRepository = bookRepository;
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = true)
    public List<BookSummary> fetchBooksWithAuthorsQueryBuilderMechanism() {
        List<BookSummary> books = bookRepository.findAllByOrderByRankAsc();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.getTitle() + ", " + b.getRank() + ", "
                + b.getAuthor().getName() + ", " + b.getAuthor().getGenre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }

    @Transactional(readOnly = true)
    public List<BookSummary> fetchBooksWithAuthorsViaQuery() {
        List<BookSummary> books = bookRepository.findByViaQuery();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.getTitle() + ", " + b.getRank() + ", "
                + b.getAuthor().getName() + ", " + b.getAuthor().getGenre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }

    @Transactional(readOnly = true)
    public List<SimpleBookSummary> fetchBooksWithAuthorsViaQuerySimpleDto() {
        List<SimpleBookSummary> books = bookRepository.findByViaQuerySimpleDto();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.getTitle() + ", " + b.getRank() + ", "
                + b.getName() + ", " + b.getGenre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }    

    @Transactional(readOnly = true)
    public List<Object[]> fetchBooksWithAuthorsViaArrayOfObjects() {
        List<Object[]> books = bookRepository.findByViaQueryArrayOfObjects();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(Arrays.toString(b)));

        briefOverviewOfPersistentContextContent();

        return books;
    }       
    
    @Transactional(readOnly = true)
    public List<VirtualBookSummary> fetchBooksWithAuthorsViaQueryVirtualDto() {
        List<VirtualBookSummary> books = bookRepository.findByViaQueryVirtualDto();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.getTitle() + ", " + b.getRank() + ", "
                + b.getAuthor().getName() + ", " + b.getAuthor().getGenre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<RecordBookSummary> fetchBooksWithAuthorsViaQueryRecordDto() {
        List<RecordBookSummary> books = bookRepository.findByViaQueryRecordDto();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.title() + ", " + b.rank() + ", "
                + b.author().name() + ", " + b.author().genre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<SimpleRecordBookSummary> fetchByViaQuerySimpleRecordDto() {
        List<SimpleRecordBookSummary> books = bookRepository.findByViaQuerySimpleRecordDto();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.title() + ", " + b.rank() + ", "
                + b.author().name() + ", " + b.author().genre()));

        briefOverviewOfPersistentContextContent();

        return books;
    }
    
    @Transactional(readOnly = true)
    public List<RecordBookView> fetchByViaQuerySimpleRecordView() {
        List<RecordBookView> books = bookRepository.findByViaQuerySimpleRecordView();

        System.out.println("\nResult set:");
        books.forEach(b -> System.out.println(b.title() + ", " + b.rank() + ", "
                + b.author().name() + ", " + b.author().genre()));

        briefOverviewOfPersistentContextContent();

        return books;
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
