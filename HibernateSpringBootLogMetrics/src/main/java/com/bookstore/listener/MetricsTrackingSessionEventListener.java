package com.bookstore.listener;

import com.bookstore.context.QueryMetricsContext;
import org.hibernate.SessionEventListener;

public class MetricsTrackingSessionEventListener implements SessionEventListener {

    private long connAcquireStartTime;
    private long prepareStartTime;
    private long executeStartTime;

    @Override
    public void jdbcConnectionAcquisitionStart() {
        connAcquireStartTime = System.nanoTime();
    }

    @Override
    public void jdbcConnectionAcquisitionEnd() {
        if (connAcquireStartTime > 0) {
            long elapsed = System.nanoTime() - connAcquireStartTime;
            connAcquireStartTime = 0;

            QueryMetricsContext.Metrics metrics = QueryMetricsContext.get();
            if (metrics != null) {
                metrics.connectionAcquireNs += elapsed;
            }
        }
    }

    @Override
    public void jdbcPrepareStatementStart() {
        prepareStartTime = System.nanoTime();
    }

    @Override
    public void jdbcPrepareStatementEnd() {
        if (prepareStartTime > 0) {
            long elapsed = System.nanoTime() - prepareStartTime;
            prepareStartTime = 0;

            QueryMetricsContext.Metrics metrics = QueryMetricsContext.get();
            if (metrics != null) {
                metrics.sqlPrepareNs += elapsed;
            }
        }
    }

    @Override
    public void jdbcExecuteStatementStart() {
        executeStartTime = System.nanoTime();
    }

    @Override
    public void jdbcExecuteStatementEnd() {
        if (executeStartTime > 0) {
            long elapsed = System.nanoTime() - executeStartTime;
            executeStartTime = 0;

            QueryMetricsContext.Metrics metrics = QueryMetricsContext.get();
            if (metrics != null) {
                metrics.sqlExecuteNs += elapsed;
                metrics.queryCount++;
            }
        }
    }

    @Override
    public void jdbcExecuteBatchStart() {
        executeStartTime = System.nanoTime();
    }

    @Override
    public void jdbcExecuteBatchEnd() {
        jdbcExecuteStatementEnd();
    }
}
