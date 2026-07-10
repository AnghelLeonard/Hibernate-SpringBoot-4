package com.bookstore.builder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationChunk<T> implements Specification<T> {

    private final Condition condition;

    public SpecificationChunk(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cquery, CriteriaBuilder cbuilder) {

        return switch (condition.getOperation()) {
            case EQUAL -> cbuilder.equal(
                    root.get(condition.getLeftHand()), condition.getRightHand());
            case NOT_EQUAL -> cbuilder.notEqual(
                    root.get(condition.getLeftHand()), condition.getRightHand());
            case GREATER_THAN -> cbuilder.greaterThan(
                    root.get(condition.getLeftHand()), condition.getRightHand());
            case LESS_THAN -> cbuilder.lessThan(
                    root.get(condition.getLeftHand()), condition.getRightHand());
            case LIKE -> cbuilder.like(
                    root.get(condition.getLeftHand()), condition.getRightHand());
            default -> null;
        };
    }
}
