package com.bookstore.naturalid;

import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;
import org.hibernate.KeyType;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public class NaturalIdRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements NaturalIdRepository<T, ID> {

    private final EntityManager entityManager;

    public NaturalIdRepositoryImpl(JpaEntityInformation entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findBySimpleNaturalId(ID naturalId) {

        // before Hibernate 7.3
        /*
        Optional<T> entity = entityManager.unwrap(Session.class)
                .bySimpleNaturalId(this.getDomainClass())
                .loadOptional(naturalId);
        */
        
        T entity = entityManager.find(this.getDomainClass(), naturalId, KeyType.NATURAL);

        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<T> findByNaturalId(Map<String, Object> naturalIds) {

        // before Hibernate 7.3
        /*
        NaturalIdLoadAccess<T> loadAccess
                = entityManager.unwrap(Session.class).byNaturalId(this.getDomainClass());
        naturalIds.forEach(loadAccess::using);
        
        return loadAccess.loadOptional();
        */
        
        T entity = entityManager.find(this.getDomainClass(), naturalIds, KeyType.NATURAL);

        return Optional.ofNullable(entity);
    }

}
