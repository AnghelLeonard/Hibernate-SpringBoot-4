package com.bookstore.listener;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import org.hibernate.SessionEventListener;

public class SlowSqlSessionEventListener implements SessionEventListener {

    private static final Logger log = LoggerFactory.getLogger(SlowSqlSessionEventListener.class);
    private static final long SLOW_SQL_THRESHOLD_NS = TimeUnit.MILLISECONDS.toNanos(1); // 1 ms

    private long statementStartTime;

    @Override
    public void jdbcExecuteStatementStart() {
        statementStartTime = System.nanoTime();
    }

    @Override
    public void jdbcExecuteStatementEnd() {
        if (statementStartTime == 0) {
            return;
        }

        long durationNs = System.nanoTime() - statementStartTime;
        statementStartTime = 0;

        if (durationNs > SLOW_SQL_THRESHOLD_NS) {
            log.warn("Slow SQL execution detected. Time: {} ms", (durationNs / 1_000_000.0));
        }
    }
}