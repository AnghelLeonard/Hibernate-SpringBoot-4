package com.bookstore.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class Dao<T, ID extends Serializable> implements GenericDao<T, ID> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public String fetchTitleAndPrice(String symbol, Instant instant) {
     
        return (String) entityManager.createQuery("""                                                  
                                                  SELECT concat_ws(:symbol, slugify(b.title), 
                                                         apply_discount(b.price, 10), :instant) 
                                                  FROM Book b WHERE b.id = 1
                                                  """
        )
                .setParameter("symbol", symbol)
                .setParameter("instant", instant)
                .getSingleResult();
    }

    @Override
    public List<String> fetchTitleAndPriceGt25(String symbol, Instant instant) {
       
        return entityManager.createQuery("""                                                  
                                        SELECT concat_ws(:symbol, slugify(b.title), b.price, :instant) 
                                        FROM Book b WHERE apply_discount(b.price, 10) > 25
                                         """
        )
                .setParameter("symbol", symbol)
                .setParameter("instant", instant)
                .getResultList();
    }   
}
