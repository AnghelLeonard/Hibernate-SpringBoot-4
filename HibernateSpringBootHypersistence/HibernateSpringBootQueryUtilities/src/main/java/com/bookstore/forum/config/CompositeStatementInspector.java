package com.bookstore.forum.config;

import io.hypersistence.utils.hibernate.query.JfrQueryLogger;
import io.hypersistence.utils.hibernate.query.QueryStackTraceLogger;
import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.List;

/**
 * Hibernate allows a single {@code StatementInspector}, so this composite runs
 * several. It has a no-arg constructor so it can be registered purely through
 * the {@code hibernate.session_factory.statement_inspector} property (see
 * {@code application.properties}) &mdash; no programmatic customizer needed.
 *
 * <p>Both delegates are debugging {@code StatementInspector}s from Hypersistence
 * Utils that observe the SQL without changing it (their {@code inspect} returns
 * {@code null}):</p>
 * <ul>
 *   <li>{@link QueryStackTraceLogger} &mdash; logs the application stack frame
 *       (here, anything under {@code com.bookstore.forum}) that triggered the
 *       query, answering "which method fired this SQL?" It logs at DEBUG.</li>
 *   <li>{@link JfrQueryLogger} &mdash; emits a JDK Flight Recorder event per
 *       query, so the SQL shows up in a JFR recording.</li>
 * </ul>
 */
public class CompositeStatementInspector implements StatementInspector {

    // tag::composite[]
    private final List<StatementInspector> inspectors = List.of(
        new QueryStackTraceLogger("com.bookstore.forum"),
        new JfrQueryLogger()
    );

    @Override
    public String inspect(String sql) {
        for (StatementInspector inspector : inspectors) {
            inspector.inspect(sql);
        }
        // Neither delegate rewrites the SQL, so signal "unchanged".
        return null;
    }
    // end::composite[]
}
