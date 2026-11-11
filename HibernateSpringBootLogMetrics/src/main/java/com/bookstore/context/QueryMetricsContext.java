package com.bookstore.context;

public class QueryMetricsContext {

    private static final ThreadLocal<Metrics> HOLDER = new ThreadLocal<>();

    public static class Metrics {
        public long connectionAcquireNs = 0; // JDBC connection acquisition from pool
        public long sqlPrepareNs = 0;        // JDBC Statement preparation
        public long sqlExecuteNs = 0;        // Database SQL / Batch execution
        public long entityCreationNs = 0;    // PreLoad -> PostLoad entity population
        public int queryCount = 0;
        public int entityCount = 0;
    }

    public static void start() {
        HOLDER.set(new Metrics());
    }

    public static Metrics get() {
        return HOLDER.get();
    }

    public static Metrics stopAndGet() {
        Metrics metrics = HOLDER.get();
        HOLDER.remove();
        return metrics;
    }
}

