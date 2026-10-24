package com.bookstore.repository;

import com.bookstore.dto.BookAuthorSearchCriteria;
import com.bookstore.entity.Author_;
import com.bookstore.entity.Book;
import com.bookstore.entity.Book_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Order;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.restriction.Path;
import org.hibernate.query.restriction.Restriction;
import org.hibernate.query.specification.SelectionSpecification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public class BookAuthorSearchRepository {
    
    @PersistenceContext
    private EntityManager entityManager;
    
     public List<Book> findBooksDynamically(BookAuthorSearchCriteria criteria) {
        
        Session session = entityManager.unwrap(Session.class);               
        
        SelectionSpecification<Book> specification = SelectionSpecification.create(
            Book.class, 
            "from Book where publicationDate > :startDate"
        );        

        if (criteria.titlePattern() != null && !criteria.titlePattern().isBlank()) {
            specification.restrict(Restriction.like(Book_.title, criteria.titlePattern()));
        }

        if (criteria.authorName() != null && !criteria.authorName().isBlank()) {
            specification.restrict(
                Path.from(Book.class).to(Book_.author).to(Author_.name)
                    .equalTo(criteria.authorName())
            );
        }
       
        if (criteria.sortByTitle()) {
            specification.sort(Order.asc(Book_.title));
        } else {
            specification.sort(Order.asc(Book_.isbn));
        }
       
        SelectionQuery<Book> query = specification.createQuery(session);
        
        // 1 Ianuarie 2021, ora 00:00:00 UTC        
        query.setParameter("startDate", ZonedDateTime.of(2021, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC).toInstant());
        
        return query.getResultList();
     }
}