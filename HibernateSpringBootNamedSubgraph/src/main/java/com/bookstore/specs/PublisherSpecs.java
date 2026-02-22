package com.bookstore.specs;

import com.bookstore.entity.Publisher;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class PublisherSpecs {

    private static final int ID = 2;

    public static Specification<Publisher> isIdGt2() {
        return (Root<Publisher> root,
                CriteriaQuery<?> query, CriteriaBuilder builder)
                -> builder.greaterThan(root.get("id"), ID);
    }
}
