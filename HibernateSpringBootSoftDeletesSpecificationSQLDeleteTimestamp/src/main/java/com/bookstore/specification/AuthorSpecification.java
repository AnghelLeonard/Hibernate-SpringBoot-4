package com.bookstore.specification;

import com.bookstore.entity.Author;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecification {
    
    public static Specification<Author> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deleted"));
    }
    
     public static Specification<Author> isDeletedOnly() {
        return (root, query, cb) -> cb.isNotNull(root.get("deleted"));
    }
     
     public static Specification<Author> deletedAfter(Instant time) {
        return (root, query, cb) -> cb.greaterThan(root.get("deleted"), time);
    }
}
