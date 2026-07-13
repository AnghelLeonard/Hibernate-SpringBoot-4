package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    public void fetch(int runs) {

        for (int i = 0; i < runs; i++) {
            authorRepository.fetchAuthorsBooksByPriceInnerJoin((int) (Math.random() * 100));            
            authorRepository.fetchByAge((int) (Math.random() * 100));
            authorRepository.fetchAuthorsBooksByPriceJoinFetch((int) (Math.random() * 100));
            briefOverviewOfQueryPlanCache();
        }        
    }

    private void briefOverviewOfQueryPlanCache() {

        System.out.println("\n-----------------------------------------------------");

        var sharedSession = entityManager.unwrap(
                SharedSessionContractImplementor.class
        );

        var statistics = sharedSession.getSessionFactory().getStatistics();
        System.out.println("Query plan cache hit count: " + statistics.getQueryPlanCacheHitCount());
        System.out.println("Query plan cache miss count: " + statistics.getQueryPlanCacheMissCount());

        System.out.println("-----------------------------------------------------\n");
    }
}
