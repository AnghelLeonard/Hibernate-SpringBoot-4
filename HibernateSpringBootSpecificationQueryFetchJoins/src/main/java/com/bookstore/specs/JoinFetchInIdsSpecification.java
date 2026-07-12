package com.bookstore.specs;

import com.bookstore.entity.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

public class JoinFetchInIdsSpecification implements Specification<Author> {   
    
    private final List<Long> ids;

    public JoinFetchInIdsSpecification(List<Long> ids) {
        this.ids = ids;
    }
            
    @Override
    public Predicate toPredicate(Root<Author> root, CriteriaQuery<?> cquery, CriteriaBuilder cbuilder) {
                           
        root.fetch("books", JoinType.LEFT);        
       
        // in case you need to add order by via Specification
        //cquery.orderBy(cbuilder.asc(root.get("...")));
        
        Expression<String> expression = root.get("id");
        return expression.in(ids);
    }
}
