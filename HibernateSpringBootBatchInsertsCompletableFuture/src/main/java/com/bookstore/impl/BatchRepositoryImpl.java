package com.bookstore.impl;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.NEVER)
public class BatchRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements BatchRepository<T, ID> {

    private final EntityManager entityManager; // this is not used

    public BatchRepositoryImpl(JpaEntityInformation entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public <S extends T> void saveInBatch(List<S> entities) {

        if (entities == null) {
            throw new IllegalArgumentException("The given Iterable of entities cannot be null!");
        }

        BatchExecutor batchExecutor = SpringContext.getBean(BatchExecutor.class);
        try {
            batchExecutor.saveInBatch(entities);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            // log exception
        } catch (ExecutionException ex) {
            // log exception
        }
    }
}
