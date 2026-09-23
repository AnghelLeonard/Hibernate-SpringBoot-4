package com.bookstore.dao;

import com.bookstore.entity.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
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

        // SELECT concat_ws(:symbol, slugify(b.title), -> works
        //        apply_discount(b.price, 10), :instant) 
        // FROM Book b WHERE b.id = 1
        
        // remain strictly JPA-compliant and portable
        return (String) entityManager.createQuery("""                                                  
                                                  SELECT function('concat_ws' as String, :symbol, 
                                                         function('slugify' as String, b.title), 
                                                         function('apply_discount' as Integer, b.price, 10), :instant) 
                                                  FROM Book b WHERE b.id = 1
                                                  """
        )
                .setParameter("symbol", symbol)
                .setParameter("instant", instant)
                .getSingleResult();
    }

    @Override
    public List<String> fetchTitleAndPriceGt25(String symbol, Instant instant) {

        // SELECT concat_ws(:symbol, slugify(b.title), b.price, :instant) -> doesn't work
        // FROM Book b WHERE apply_discount(b.price, 10) > 25
        
        // SELECT concat_ws(:symbol, slugify(b.title), b.price, :instant) -> works
        // FROM Book b WHERE function('apply_discount', b.price, 10) > 25
        
        // remain strictly JPA-compliant and portable
        return entityManager.createQuery("""                                                  
                                        SELECT function('concat_ws' as String, :symbol, 
                                         function('slugify' as String, b.title), b.price, :instant) 
                                        FROM Book b WHERE function('apply_discount' as Integer, b.price, 10) > 25
                                         """
        )
                .setParameter("symbol", symbol)
                .setParameter("instant", instant)
                .getResultList();
    }

    @Override
    public String fetchTitleAndPriceCb(String symbol, Instant instant) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<Book> book = cq.from(Book.class);
        Expression<String> title = book.get("title");
        Expression<Integer> price = book.get("price");
        Expression<Long> id = book.get("id");
        
        cq.select(cb.function("concat_ws", String.class, cb.literal(symbol), 
                cb.function("slugify", String.class, title), 
                cb.function("apply_discount", Integer.class, price, cb.literal(10)), cb.literal(instant)))
                .where(id.equalTo(cb.literal(1)));

        return entityManager.createQuery(cq).getSingleResult();
    }

    @Override
    public List<String> fetchTitleAndPriceGt25Cb(String symbol, Instant instant) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);

        Root<Book> book = cq.from(Book.class);
        Expression<String> title = book.get("title");
        Expression<Integer> price = book.get("price");        
        
        cq.select(cb.function("concat_ws", String.class, cb.literal(symbol), 
                cb.function("slugify", String.class, title), price, cb.literal(instant)))
                .where(cb.greaterThan(cb.function("apply_discount", Integer.class, price, cb.literal(10)), cb.literal(25)));

        return entityManager.createQuery(cq).getResultList();
    }
}
