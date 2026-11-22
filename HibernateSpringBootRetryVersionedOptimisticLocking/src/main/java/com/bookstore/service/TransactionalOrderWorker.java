package com.bookstore.service;

import com.bookstore.entity.BookOrder;
import com.bookstore.entity.IdempotencyRecord;
import com.bookstore.entity.Inventory;
import com.bookstore.repository.BookOrderRepository;
import com.bookstore.repository.IdempotencyRecordRepository;
import com.bookstore.repository.InventoryRepository;
import io.hypersistence.tsid.TSID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.core.JacksonException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import tools.jackson.databind.ObjectMapper;

@Service
public class TransactionalOrderWorker {

    private static final Logger log = LoggerFactory.getLogger(TransactionalOrderWorker.class);
    private static final Duration IDEMPOTENCY_TTL = Duration.ofHours(24);

    private final InventoryRepository inventoryRepository;
    private final BookOrderRepository orderRepository;
    private final IdempotencyRecordRepository idempotencyRepository;
    private final ObjectMapper objectMapper;

    public TransactionalOrderWorker(InventoryRepository inventoryRepository,
                                    BookOrderRepository orderRepository,
                                    IdempotencyRecordRepository idempotencyRepository,
                                    ObjectMapper objectMapper) {
        this.inventoryRepository = inventoryRepository;
        this.orderRepository = orderRepository;
        this.idempotencyRepository = idempotencyRepository;
        this.objectMapper = objectMapper;
    }

    @Retryable(
        includes = ObjectOptimisticLockingFailureException.class,
        maxRetries = 3,
        delay = 50,
        multiplier = 2,
        jitter = 25
    )
    @Transactional
    public OrderExecutionResult executeOrder(String idempotencyKey, String incomingHash,
                                             String email, String sku, int qty) {
        log.debug("Executing transactional order for {} ({}x {})", email, qty, sku);

        // clean up expired record if the same key is reused after TTL
        idempotencyRepository.findById(idempotencyKey).ifPresent(existing -> {
            if (existing.isExpired()) {
                idempotencyRepository.delete(existing);
                idempotencyRepository.flush();
            }
        });

        Inventory inventory = inventoryRepository.findBySku(sku)
                .orElseThrow(() -> new IllegalArgumentException("Unknown SKU: " + sku));

        inventory.reserve(qty); // optimistic lock (@Version) checked on flush/commit
        
        BookOrder order = new BookOrder();
        order.setId(TSID.Factory.getTsid().toLong()); // manually assigned 64-bit time-sorted ID
        order.setCustomerEmail(email);
        order.setCreatedAt(Instant.now());
        order.setQuantity(qty);
        order.setSku(sku);
        
        BookOrder savedOrder = orderRepository.save(order);
                
        String responseJson = serialize(savedOrder);

        IdempotencyRecord record = new IdempotencyRecord();
        record.setIdempotencyKey(idempotencyKey);
        record.setRequestHash(incomingHash);
        record.setResponseStatus(HttpStatus.CREATED.value());
        record.setResponseBody(responseJson);
        record.setExpiresAt(IDEMPOTENCY_TTL);
                
        idempotencyRepository.saveAndFlush(record);

        return new OrderExecutionResult(HttpStatus.CREATED.value(), responseJson, false);
    }

    private String serialize(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JacksonException ex) {
            throw new IllegalStateException("Failed to serialize order response", ex);
        }
    }

    public record OrderExecutionResult(int statusCode, String jsonBody, boolean replayed) {}
}

