package com.bookstore.aop;

import com.bookstore.context.QueryMetricsContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class UnifiedQueryProfilerAspect {

    private static final Logger log = LoggerFactory.getLogger(UnifiedQueryProfilerAspect.class);
    private static final long SLOW_THRESHOLD_NS = TimeUnit.MILLISECONDS.toNanos(1); // 1ms, but use 50 ms for real

    @Around("execution(* org.springframework.data.repository.Repository+.*(..))")
    public Object profileRepositoryCall(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isOuterCall = (QueryMetricsContext.get() == null);
        if (isOuterCall) {
            QueryMetricsContext.start();
        }

        long totalStart = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long totalDurationNs = System.nanoTime() - totalStart;

            if (isOuterCall) {
                QueryMetricsContext.Metrics metrics = QueryMetricsContext.stopAndGet();

                if (totalDurationNs > SLOW_THRESHOLD_NS && metrics != null) {
                    double totalMs = totalDurationNs / 1_000_000.0;
                    double connMs = metrics.connectionAcquireNs / 1_000_000.0;
                    double prepareMs = metrics.sqlPrepareNs / 1_000_000.0;
                    double executeMs = metrics.sqlExecuteNs / 1_000_000.0;
                    double entityMs = metrics.entityCreationNs / 1_000_000.0;
                    double ormOverheadMs = Math.max(0, totalMs - connMs - prepareMs - executeMs - entityMs);

                    log.warn(
                        "Slow Repository Call: {}.{}() | Total: {} ms (Conn Acquire: {} ms, Prepare: {} ms, SQL Exec: {} ms, Entity Creation: {} ms, ResultSet & ORM Overhead: {} ms | Queries: {}, Entities: {})",
                        joinPoint.getSignature().getDeclaringType().getSimpleName(),
                        joinPoint.getSignature().getName(),
                        String.format("%.2f", totalMs),
                        String.format("%.2f", connMs),
                        String.format("%.2f", prepareMs),
                        String.format("%.2f", executeMs),
                        String.format("%.2f", entityMs),
                        String.format("%.2f", ormOverheadMs),
                        metrics.queryCount,
                        metrics.entityCount
                    );
                }
            }
        }
    }
}
