package com.bookstore.specs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class JoinFetchSpecification<Author> implements Specification<Author> {   
    
    private final String genre;

    public JoinFetchSpecification(String genre) {
        this.genre = genre;
    }
            
    @Override
    public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> cquery, CriteriaBuilder cbuilder) {

        // This is needed to support Pageable queries
        // This causes pagination in memory (HHH90003004)
        Class clazz = cquery.getResultType();
        if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return null;
        }
       
        root.fetch("books", JoinType.LEFT);        
        
        // in case you need to add order by via Specification
        //cquery.orderBy(cbuilder.asc(root.get("...")));
        
        return cbuilder.equal(root.get("genre"), genre);
    }
}
