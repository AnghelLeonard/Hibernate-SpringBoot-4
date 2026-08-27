package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import com.bookstore.dto.BookstoreDto;
import com.bookstore.dto.BookstoreRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.Map;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository, EntityManager entityManager) {
        this.authorRepository = authorRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public List<BookstoreDto> fetchAuthorsDto() {

        briefOverviewOfPersistentContextContent();

        List<BookstoreDto> dto = authorRepository.fetchAllDto();

        briefOverviewOfPersistentContextContent();

        // the fetched Author are managed by Hibernate
        // the following line of code will trigger an UPDATE
        dto.get(0).getAuthor().setAge(47);
        
        return dto;
    }
    
    @Transactional
    public List<BookstoreRecord> fetchAuthorsRecord() {

        briefOverviewOfPersistentContextContent();

        List<BookstoreRecord> dto = authorRepository.fetchAllRecord();

        briefOverviewOfPersistentContextContent();

        // the fetched Author are managed by Hibernate
        // the following line of code will trigger an UPDATE
        dto.get(0).author().setAge(44);
        
        return dto;
    }

    private void briefOverviewOfPersistentContextContent() {

        System.out.println("\n-----------------------------------------------------");

        org.hibernate.engine.spi.PersistenceContext persistenceContext = getPersistenceContext();

        int managedEntities = persistenceContext.getNumberOfManagedEntities();
        int collectionEntriesSize = persistenceContext.getCollectionEntriesSize();

        System.out.println("Total number of managed entities: "
                + managedEntities);
        System.out.println("Total number of collection entries: "
                + collectionEntriesSize);

        // getEntitiesByKey() will be removed and probably replaced with #iterateEntities() 
        Map<EntityKey, Object> entitiesByKey = persistenceContext.getEntitiesByKey();

        if (!entitiesByKey.isEmpty()) {
            System.out.println("\nEntities by key:");
            entitiesByKey.forEach((key, value) -> System.out.println(key + ": " + value));

            System.out.println("\nStatus and hydrated state:");
            for (Object entry : entitiesByKey.values()) {
                EntityEntry ee = persistenceContext.getEntry(entry);
                System.out.println(
                        "Entity name: " + ee.getEntityName()
                        + " | Status: " + ee.getStatus()
                        + " | State: " + Arrays.toString(ee.getLoadedState()));
            }
        }

        System.out.println("-----------------------------------------------------\n");
    }

    private org.hibernate.engine.spi.PersistenceContext getPersistenceContext() {

        SharedSessionContractImplementor sharedSession = entityManager.unwrap(
                SharedSessionContractImplementor.class
        );

        return sharedSession.getPersistenceContext();
    }
}
