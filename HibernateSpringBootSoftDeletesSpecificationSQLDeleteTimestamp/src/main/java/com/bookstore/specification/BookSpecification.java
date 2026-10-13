package com.bookstore.specification;

import com.bookstore.entity.Book;
import java.time.Instant;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    
    public static Specification<Book> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deleted"));
    }
    
     public static Specification<Book> isDeletedOnly() {
        return (root, query, cb) -> cb.isNotNull(root.get("deleted"));
    }
     
     public static Specification<Book> deletedAfter(Instant time) {
        return (root, query, cb) -> cb.greaterThan(root.get("deleted"), time);
    }
}
