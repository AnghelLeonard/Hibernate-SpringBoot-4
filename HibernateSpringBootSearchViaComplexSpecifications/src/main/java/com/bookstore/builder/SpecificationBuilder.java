package com.bookstore.builder;

import com.bookstore.builder.Condition.LogicalOperatorType;
import com.bookstore.builder.Condition.OperationType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationBuilder<T> {

    private record SpecNode<T>(Specification<T> spec, LogicalOperatorType operator) {}

    private final List<SpecNode<T>> nodes = new ArrayList<>();

    public SpecificationBuilder<T> with(String leftHand, Object rightHand,
            OperationType operation, LogicalOperatorType operator) {
        Condition condition = new Condition(leftHand, rightHand, operation, operator);
        nodes.add(new SpecNode<>(new SpecificationChunk<>(condition), operator));
        return this;
    }

    public SpecificationBuilder<T> withNot(String leftHand, Object rightHand,
            OperationType operation, LogicalOperatorType operator) {
        Condition condition = new Condition(leftHand, rightHand, List.of(), operation, operator, true);
        nodes.add(new SpecNode<>(new SpecificationChunk<>(condition), operator));
        return this;
    }

    public SpecificationBuilder<T> withValues(String leftHand, List<Object> values,
            OperationType operation, LogicalOperatorType operator) {
        Condition condition = new Condition(leftHand, null, values, operation, operator, false);
        nodes.add(new SpecNode<>(new SpecificationChunk<>(condition), operator));
        return this;
    }

    public SpecificationBuilder<T> withGroup(Consumer<SpecificationBuilder<T>> groupConsumer,
            LogicalOperatorType operator) {
        SpecificationBuilder<T> nestedBuilder = new SpecificationBuilder<>();
        groupConsumer.accept(nestedBuilder);
        Specification<T> groupSpec = nestedBuilder.build();
        if (groupSpec != null) {
            nodes.add(new SpecNode<>(groupSpec, operator));
        }
        return this;
    }

    public SpecificationBuilder<T> withSpec(Specification<T> spec, LogicalOperatorType operator) {
        if (spec != null) {
            nodes.add(new SpecNode<>(spec, operator));
        }
        return this;
    }

    public Specification<T> build() {
        if (nodes.isEmpty()) {
            return null;
        }

        Specification<T> result = Specification.where(nodes.get(0).spec());

        for (int i = 1; i < nodes.size(); i++) {
            LogicalOperatorType prevOp = nodes.get(i - 1).operator();
            if (prevOp == LogicalOperatorType.END) {
                break;
            }
            Specification<T> currentSpec = nodes.get(i).spec();
            result = (prevOp == LogicalOperatorType.OR)
                    ? result.or(currentSpec)
                    : result.and(currentSpec);
        }

        return result;
    }
}
