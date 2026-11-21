package com.bookstore.builder;

import com.bookstore.builder.Condition.LogicalOperatorType;
import com.bookstore.builder.Condition.OperationType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SpecificationParser {

    // Tokenizes "(", ")", "AND", "OR", or "field:op:value" / "field:op"
    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\(|\\)|\\bAND\\b|\\bOR\\b|[^\\s()]+",
            Pattern.CASE_INSENSITIVE
    );

    public <T> Specification<T> parse(String filterQuery, Set<String> allowedFields) {
        if (filterQuery == null || filterQuery.isBlank()) {
            return null;
        }

        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(filterQuery.trim());
        while (matcher.find()) {
            tokens.add(matcher.group());
        }

        int[] pos = {0};
        Specification<T> spec = parseExpression(tokens, pos, allowedFields, 0);

        if (pos[0] < tokens.size()) {
            throw new IllegalArgumentException(
                    "Unexpected token '" + tokens.get(pos[0]) + "' at token index " + pos[0]);
        }

        return spec;
    }

    private <T> Specification<T> parseExpression(
            List<String> tokens, int[] pos, Set<String> allowedFields, int depth) {

        SpecificationBuilder<T> builder = new SpecificationBuilder<>();
        boolean closedBracket = false;
        boolean hasTerms = false;

        while (pos[0] < tokens.size()) {
            String token = tokens.get(pos[0]);

            if (")".equals(token)) {
                if (depth == 0) {
                    throw new IllegalArgumentException("Unmatched closing ')' at token index " + pos[0]);
                }
                if (!hasTerms) {
                    throw new IllegalArgumentException("Empty parentheses '()' at token index " + pos[0]);
                }
                pos[0]++; // consume ')'
                closedBracket = true;
                break;
            }

            if ("AND".equalsIgnoreCase(token) || "OR".equalsIgnoreCase(token)) {
                throw new IllegalArgumentException(
                        "Unexpected logical operator '" + token + "' at token index " + pos[0]);
            }

            Specification<T> termSpec;
            if ("(".equals(token)) {
                pos[0]++; // consume '('
                termSpec = parseExpression(tokens, pos, allowedFields, depth + 1);
            } else {
                termSpec = parseSingleCondition(token, allowedFields);
                pos[0]++;
            }

            hasTerms = true;

            LogicalOperatorType nextOp = LogicalOperatorType.END;
            if (pos[0] < tokens.size()) {
                String lookahead = tokens.get(pos[0]).toUpperCase();
                if ("AND".equals(lookahead)) {
                    nextOp = LogicalOperatorType.AND;
                    pos[0]++;
                } else if ("OR".equals(lookahead)) {
                    nextOp = LogicalOperatorType.OR;
                    pos[0]++;
                } else if (!")".equals(lookahead)) {
                    throw new IllegalArgumentException(
                            "Expected 'AND', 'OR', or ')' before '" + tokens.get(pos[0]) + "'");
                }
            }

            if (nextOp != LogicalOperatorType.END && pos[0] >= tokens.size()) {
                throw new IllegalArgumentException(
                        "Trailing logical operator '" + nextOp + "' with no following condition");
            }

            builder.withSpec(termSpec, nextOp);
        }

        if (depth > 0 && !closedBracket) {
            throw new IllegalArgumentException("Missing closing ')' for open parenthesis");
        }

        return builder.build();
    }

    private <T> Specification<T> parseSingleCondition(String token, Set<String> allowedFields) {
        String[] parts = token.split(":", 3);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid filter token: '" + token + "'. Expected 'field:op[:value]'");
        }

        String field = parts[0];
        if (allowedFields != null && !allowedFields.contains(field)) {
            throw new IllegalArgumentException("Filtering on field '" + field + "' is not allowed");
        }

        OperationType op = mapOperation(parts[1]);
        String rawVal = parts.length == 3 ? parts[2] : null;

        Condition condition = switch (op) {
            case IS_NULL, IS_NOT_NULL -> {
                if (rawVal != null && !rawVal.isEmpty()) {
                    throw new IllegalArgumentException("Operation " + op + " does not accept a value");
                }
                yield new Condition(field, null, List.of(), op, LogicalOperatorType.END, false);
            }
            case IN, NOT_IN -> {
                if (rawVal == null || rawVal.isBlank()) {
                    throw new IllegalArgumentException("Operation " + op + " requires values separated by ',' or '|'");
                }
                List<Object> listValues = Arrays.asList((Object[]) rawVal.split("[,|]"));
                yield new Condition(field, null, listValues, op, LogicalOperatorType.END, false);
            }
            case BETWEEN -> {
                if (rawVal == null || rawVal.isBlank()) {
                    throw new IllegalArgumentException("Operation BETWEEN requires two values separated by ',' or '|'");
                }
                List<Object> rangeValues = Arrays.asList((Object[]) rawVal.split("[,|]"));
                if (rangeValues.size() != 2) {
                    throw new IllegalArgumentException("Operation BETWEEN requires exactly 2 values separated by ',' or '|'");
                }
                yield new Condition(field, null, rangeValues, op, LogicalOperatorType.END, false);
            }
            default -> {
                if (rawVal == null || rawVal.isEmpty()) {
                    throw new IllegalArgumentException("Operation " + op + " requires a value");
                }
                yield new Condition(field, rawVal, List.of(), op, LogicalOperatorType.END, false);
            }
        };

        return new SpecificationChunk<>(condition);
    }

    private OperationType mapOperation(String code) {
        return switch (code.toLowerCase()) {
            case "eq" ->
                OperationType.EQUAL;
            case "neq" ->
                OperationType.NOT_EQUAL;
            case "gt" ->
                OperationType.GREATER_THAN;
            case "gte" ->
                OperationType.GREATER_THAN_OR_EQUAL;
            case "lt" ->
                OperationType.LESS_THAN;
            case "lte" ->
                OperationType.LESS_THAN_OR_EQUAL;
            case "like" ->
                OperationType.LIKE;
            case "ilike" ->
                OperationType.ILIKE;
            case "in" ->
                OperationType.IN;
            case "nin" ->
                OperationType.NOT_IN;
            case "between" ->
                OperationType.BETWEEN;
            case "isnull" ->
                OperationType.IS_NULL;
            case "notnull" ->
                OperationType.IS_NOT_NULL;
            default ->
                throw new IllegalArgumentException("Unsupported operation: '" + code + "'");
        };
    }
}
