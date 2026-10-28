package com.bookstore.service;

import com.bookstore.dto.AuthorSummary;
import com.bookstore.dto.RecordAuthorSummary;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.EntityKey;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.springframework.stereotype.Service;
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

    @Transactional(readOnly = true)
    public List<Author> fetchViafindAll() {
        List<Author> authors = authorRepository.findAll();

        authors.forEach(author -> System.out.println(author));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchViafindBy() {
        List<AuthorSummary> authors = authorRepository.findBy();

        authors.forEach(author -> System.out.println("Id: " + author.getId()
                + ", Age: " + author.getAge() + ", Name: " + author.getName()
                + ", Genre: " + author.getGenre() + ", Address: " + author.getAddress()
                + ", Rank: " + author.getRank() + ", Nickname: " + author.getNickname()
                + ", Language: " + author.getLanguage()));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAsDto() {
        List<AuthorSummary> authors = authorRepository.fetchAsDto();

        authors.forEach(author -> System.out.println("Id: " + author.getId()
                + ", Age: " + author.getAge() + ", Name: " + author.getName()
                + ", Genre: " + author.getGenre() + ", Address: " + author.getAddress()
                + ", Rank: " + author.getRank() + ", Nickname: " + author.getNickname()
                + ", Language: " + author.getLanguage()));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAsDtoColumns() {
        List<AuthorSummary> authors = authorRepository.fetchAsDtoColumns();

        authors.forEach(author -> System.out.println("Id: " + author.getId()
                + ", Age: " + author.getAge() + ", Name: " + author.getName()
                + ", Genre: " + author.getGenre() + ", Address: " + author.getAddress()
                + ", Rank: " + author.getRank() + ", Nickname: " + author.getNickname()
                + ", Language: " + author.getLanguage()));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<AuthorSummary> fetchAsDtoNative() {
        List<AuthorSummary> authors = authorRepository.fetchAsDtoNative();

        authors.forEach(author -> System.out.println("Id: " + author.getId()
                + ", Age: " + author.getAge() + ", Name: " + author.getName()
                + ", Genre: " + author.getGenre() + ", Address: " + author.getAddress()
                + ", Rank: " + author.getRank() + ", Nickname: " + author.getNickname()
                + ", Language: " + author.getLanguage()));

        briefOverviewOfPersistentContextContent();

        return authors;
    }

    @Transactional(readOnly = true)
    public List<RecordAuthorSummary> fetchAsRecordDto() {
        List<RecordAuthorSummary> authors = authorRepository.fetchAsRecordDto();

        authors.forEach(author -> System.out.printf(
                "Id: %d, Age: %d, Name: %s, Genre: %s, Address: %s, Rank: %d, Nickname: %s, Language: %s%n",
                author.id(), author.age(), author.name(), author.genre(), author.address(), author.rank(),
                author.nickname(), author.language()
        ));

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
