package com.bookstore.multipleids;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import org.hibernate.BatchSize;
import org.hibernate.Session;
import org.hibernate.SessionCheckMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public abstract class MultipleIdsRepositoryImpl<T, ID extends Serializable>
        implements MultipleIdsRepository<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    private final Class<T> entityClass;

    public MultipleIdsRepositoryImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<T> fetchByMultipleIds(List<ID> ids) {

        Session session = entityManager.unwrap(Session.class);
        
        List<T> result = session.findMultiple(entityClass, ids);                

        return result;
    }

    @Override
    public List<T> fetchInBatchesByMultipleIds(List<ID> ids, int batchSize) {

        Session session = entityManager.unwrap(Session.class);
        
        List<T> result = session.findMultiple(entityClass, ids, new BatchSize(batchSize));                

        return result;
    }

    @Override
    public List<T> fetchBySessionCheckMultipleIds(List<ID> ids) {

        Session session = entityManager.unwrap(Session.class);
        
        List<T> result = session.findMultiple(entityClass, ids, SessionCheckMode.ENABLED);                

        return result;
    }

    @Override
    public List<T> fetchInBatchesBySessionCheckMultipleIds(List<ID> ids, int batchSize) {

        Session session = entityManager.unwrap(Session.class);
        
        List<T> result = session.findMultiple(entityClass, ids, 
                new BatchSize(batchSize), SessionCheckMode.ENABLED);                

        return result;
    }
}
