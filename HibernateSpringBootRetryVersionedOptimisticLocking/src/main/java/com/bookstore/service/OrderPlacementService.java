package com.bookstore.service;

import com.bookstore.entity.IdempotencyRecord;
import com.bookstore.exception.HighContentionException;
import com.bookstore.exception.IdempotencyPayloadMismatchException;
import com.bookstore.repository.IdempotencyRecordRepository;
import com.bookstore.service.TransactionalOrderWorker.OrderExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderPlacementService {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacementService.class);

    private final TransactionalOrderWorker orderWorker;
    private final IdempotencyRecordRepository idempotencyRepository;
    private final RequestHashService hashService;

    public OrderPlacementService(TransactionalOrderWorker orderWorker,
                                 IdempotencyRecordRepository idempotencyRepository,
                                 RequestHashService hashService) {
        this.orderWorker = orderWorker;
        this.idempotencyRepository = idempotencyRepository;
        this.hashService = hashService;
    }

    public OrderExecutionResult placeOrder(String idempotencyKey, String email, String sku, int qty) {
        String incomingHash = hashService.computeHash(email, sku, qty);

        // fast-path replay check
        Optional<IdempotencyRecord> existing = idempotencyRepository.findById(idempotencyKey);
        if (existing.isPresent() && !existing.get().isExpired()) {
            return verifyAndReplay(existing.get(), incomingHash);
        }

        try {
            // delegate to proxied @Retryable + @Transactional worker
            return orderWorker.executeOrder(idempotencyKey, incomingHash, email, sku, qty);

        } catch (ObjectOptimisticLockingFailureException ex) {
            // maxRetries (3) are exhausted
            log.error("Exhausted retries due to high contention on SKU {}", sku);
            throw new HighContentionException(
                "System is currently busy processing orders for " + sku + ". Please try again.", ex
            );
        } catch (DataIntegrityViolationException ex) {
            // concurrent duplicate request caught by primary key constraint
            log.warn("Concurrent duplicate request detected for key {}", idempotencyKey);
            IdempotencyRecord winner = idempotencyRepository.findById(idempotencyKey)
                    .orElseThrow(() -> ex);
            return verifyAndReplay(winner, incomingHash);
        }
    }

    private OrderExecutionResult verifyAndReplay(IdempotencyRecord record, String incomingHash) {
        if (!record.getRequestHash().equals(incomingHash)) {
            throw new IdempotencyPayloadMismatchException(
                "Idempotency-Key was already used with a different request payload."
            );
        }
        return new OrderExecutionResult(HttpStatus.OK.value(), record.getResponseBody(), true);
    }
}