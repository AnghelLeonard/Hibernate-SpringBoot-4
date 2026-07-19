package com.bookstore.forum.config;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

/**
 * Evaluates {@link EnabledIfDatabaseAvailable} before the Spring context is
 * bootstrapped, so an unreachable database results in a skipped test rather
 * than a context-load failure.
 */
public class DatabaseAvailableCondition implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        return AnnotationSupport.findAnnotation(
                context.getElement(), EnabledIfDatabaseAvailable.class)
            .map(annotation -> {
                DatabaseType databaseType = annotation.value();
                return DatabaseAvailability.isAvailable(databaseType)
                    ? ConditionEvaluationResult.enabled(
                        databaseType + " is available")
                    : ConditionEvaluationResult.disabled(
                        databaseType + " is not reachable (no local server and no Docker)");
            })
            .orElseGet(() -> ConditionEvaluationResult.enabled(
                "No @EnabledIfDatabaseAvailable present"));
    }
}
