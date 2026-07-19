package com.bookstore.forum.config;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables a test class or method only when the given {@link DatabaseType} is
 * reachable (locally or through the Testcontainers fallback). This keeps the
 * portability examples green on any machine: whichever databases are up run,
 * the rest are skipped instead of failing.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DatabaseAvailableCondition.class)
public @interface EnabledIfDatabaseAvailable {

    DatabaseType value();
}
