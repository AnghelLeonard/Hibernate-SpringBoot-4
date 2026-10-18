package com.bookstore.specifications;

import com.bookstore.entity.Book;
import com.bookstore.entity.Ebook;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    
    public static Specification<Book> findEbookOfFormatSpec(String format) {
        return (root, query, cb) -> {           
            return cb.equal(
                cb.treat(root, Ebook.class).get("format"), 
                format
            );
        };
    }
}
