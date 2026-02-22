package com.bookstore.specs;

import com.bookstore.entity.Author;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecs {

    private static final int AGE = 45;

    public static Specification<Author> isAgeGt45() {
        return (Root<Author> root,
                CriteriaQuery<?> query, CriteriaBuilder builder)
                -> builder.greaterThan(root.get("age"), AGE);
    }
}
