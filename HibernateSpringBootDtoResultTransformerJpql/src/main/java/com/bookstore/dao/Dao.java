package com.bookstore.dao;

import com.bookstore.dto.AuthorDtoNoSetters;
import com.bookstore.dto.AuthorDtoWithSetters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class Dao implements AuthorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDtoNoSetters> fetchAuthorsNoSetters() {
        List<AuthorDtoNoSetters> authors = entityManager
                .createQuery("SELECT a.name as name, a.age as age FROM Author a")
                .unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuples, aliases) -> {
                    AuthorDtoNoSetters authorDTO
                            = new AuthorDtoNoSetters((String) tuples[0], (int) tuples[1]);

                    return authorDTO;
                }).getResultList();

        return authors;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorDtoWithSetters> fetchAuthorsWithSetters() {
        List<AuthorDtoWithSetters> authors = entityManager
                .createQuery("SELECT a.name as name, a.age as age FROM Author a")
                .unwrap(org.hibernate.query.Query.class)
                .setTupleTransformer((tuples, aliases) -> {
                    AuthorDtoWithSetters authorDTO = new AuthorDtoWithSetters();
                    authorDTO.setName((String) tuples[0]);
                    authorDTO.setAge((int) tuples[1]);

                    return authorDTO;
                }).getResultList();

        return authors;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
