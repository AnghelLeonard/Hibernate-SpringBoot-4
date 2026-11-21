package com.bookstore.builder;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationChunk<T> implements Specification<T> {

    private final Condition condition;

    public SpecificationChunk(Condition condition) {
        this.condition = condition;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> cquery, CriteriaBuilder cbuilder) {
        Path<?> path = resolvePath(root, cquery, condition.getLeftHand());
        Class<?> targetType = path.getJavaType();

        Predicate predicate = switch (condition.getOperation()) {
            case EQUAL ->
                cbuilder.equal(path, castValue(targetType, condition.getRightHand()));
            case NOT_EQUAL ->
                cbuilder.notEqual(path, castValue(targetType, condition.getRightHand()));
            case GREATER_THAN ->
                cbuilder.greaterThan(
                (Path<Comparable>) path, (Comparable) castValue(targetType, condition.getRightHand()));
            case GREATER_THAN_OR_EQUAL ->
                cbuilder.greaterThanOrEqualTo(
                (Path<Comparable>) path, (Comparable) castValue(targetType, condition.getRightHand()));
            case LESS_THAN ->
                cbuilder.lessThan(
                (Path<Comparable>) path, (Comparable) castValue(targetType, condition.getRightHand()));
            case LESS_THAN_OR_EQUAL ->
                cbuilder.lessThanOrEqualTo(
                (Path<Comparable>) path, (Comparable) castValue(targetType, condition.getRightHand()));
            case LIKE ->
                cbuilder.like(path.as(String.class), condition.getRightHand().toString());
            case ILIKE ->
                cbuilder.like(
                cbuilder.lower(path.as(String.class)),
                condition.getRightHand().toString().toLowerCase());
            case IN -> {
                CriteriaBuilder.In<Object> inClause = cbuilder.in(path);
                for (Object val : condition.getValues()) {
                    inClause.value(castValue(targetType, val));
                }
                yield inClause;
            }
            case NOT_IN -> {
                CriteriaBuilder.In<Object> inClause = cbuilder.in(path);
                for (Object val : condition.getValues()) {
                    inClause.value(castValue(targetType, val));
                }
                yield cbuilder.not(inClause);
            }
            case BETWEEN -> {
                List<Object> vals = condition.getValues();
                if (vals.size() != 2) {
                    throw new IllegalArgumentException("BETWEEN requires exactly 2 values");
                }
                Comparable start = (Comparable) castValue(targetType, vals.get(0));
                Comparable end = (Comparable) castValue(targetType, vals.get(1));
                yield cbuilder.between((Path<Comparable>) path, start, end);
            }
            case IS_NULL ->
                cbuilder.isNull(path);
            case IS_NOT_NULL ->
                cbuilder.isNotNull(path);
        };

        return condition.isNegated() ? cbuilder.not(predicate) : predicate;
    }

    private Path<?> resolvePath(Root<T> root, CriteriaQuery<?> query, String fieldPath) {
        if (!fieldPath.contains(".")) {
            return root.get(fieldPath);
        }

        String[] parts = fieldPath.split("\\.");
        From<?, ?> current = root;

        for (int i = 0; i < parts.length - 1; i++) {
            String association = parts[i];
            current = getOrCreateJoin(current, association);
            if (query != null && Long.class != query.getResultType() && long.class != query.getResultType()) {
                query.distinct(true);
            }
        }

        return current.get(parts[parts.length - 1]);
    }

    private Join<?, ?> getOrCreateJoin(From<?, ?> from, String attribute) {
        for (Join<?, ?> existing : from.getJoins()) {
            if (existing.getAttribute().getName().equals(attribute)) {
                return existing;
            }
        }
        return from.join(attribute, JoinType.INNER);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object castValue(Class<?> targetType, Object rawValue) {
        if (rawValue == null || targetType.isInstance(rawValue)) {
            return rawValue;
        }
        String text = rawValue.toString();
        if (targetType == Integer.class || targetType == int.class) {
            return Integer.valueOf(text);
        } else if (targetType == Long.class || targetType == long.class) {
            return Long.valueOf(text);
        } else if (targetType == Double.class || targetType == double.class) {
            return Double.valueOf(text);
        } else if (targetType == BigDecimal.class) {
            return new BigDecimal(text);
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return Boolean.valueOf(text);
        } else if (targetType == LocalDate.class) {
            return LocalDate.parse(text);
        } else if (targetType == Instant.class) {
            return Instant.parse(text);
        } else if (targetType.isEnum()) {
            return Enum.valueOf((Class<Enum>) targetType, text.toUpperCase());
        }
        return text;
    }
}
