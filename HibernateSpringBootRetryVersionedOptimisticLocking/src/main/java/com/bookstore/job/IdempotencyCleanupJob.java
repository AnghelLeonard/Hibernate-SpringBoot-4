package com.bookstore.job;

import com.bookstore.repository.IdempotencyRecordRepository;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class IdempotencyCleanupJob {

    private static final Logger log = LoggerFactory.getLogger(IdempotencyCleanupJob.class);
    private final IdempotencyRecordRepository idempotencyRepository;

    public IdempotencyCleanupJob(IdempotencyRecordRepository idempotencyRepository) {
        this.idempotencyRepository = idempotencyRepository;
    }

    @Scheduled(cron = "0 0 * * * *") // top of every hour
    @Transactional
    public void purgeExpiredKeys() {
        int deleted = idempotencyRepository.deleteExpiredRecords(Instant.now());
        if (deleted > 0) {
            log.info("Purged {} expired idempotency keys", deleted);
        }
    }
}
