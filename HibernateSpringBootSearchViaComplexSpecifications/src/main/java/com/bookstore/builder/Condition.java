package com.bookstore.builder;

import java.util.Collections;
import java.util.List;

public final class Condition {

    public enum LogicalOperatorType {
        AND, OR, END
    }

    public enum OperationType {
        EQUAL,
        NOT_EQUAL,
        GREATER_THAN,
        GREATER_THAN_OR_EQUAL,
        LESS_THAN,
        LESS_THAN_OR_EQUAL,
        LIKE,
        ILIKE,
        IN,
        NOT_IN,
        BETWEEN,
        IS_NULL,
        IS_NOT_NULL
    }

    private final String leftHand;
    private final Object rightHand;
    private final List<Object> values;
    private final OperationType operation;
    private final LogicalOperatorType operator;
    private final boolean negated;

    public Condition(String leftHand, Object rightHand,
            OperationType operation, LogicalOperatorType operator) {
        this(leftHand, rightHand, Collections.emptyList(), operation, operator, false);
    }

    public Condition(String leftHand, Object rightHand, List<Object> values,
            OperationType operation, LogicalOperatorType operator, boolean negated) {
        this.leftHand = leftHand;
        this.rightHand = rightHand;
        this.values = values == null ? Collections.emptyList() : List.copyOf(values);
        this.operation = operation;
        this.operator = operator;
        this.negated = negated;
    }

    public String getLeftHand() {
        return leftHand;
    }

    public Object getRightHand() {
        return rightHand;
    }

    public List<Object> getValues() {
        return values;
    }

    public OperationType getOperation() {
        return operation;
    }

    public LogicalOperatorType getOperator() {
        return operator;
    }

    public boolean isNegated() {
        return negated;
    }
}
