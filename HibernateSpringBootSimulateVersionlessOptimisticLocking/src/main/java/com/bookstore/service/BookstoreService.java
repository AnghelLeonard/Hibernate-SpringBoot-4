package com.bookstore.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private static final Logger log = LoggerFactory.getLogger(BookstoreService.class);
    private final InventoryService inventoryService;

    public BookstoreService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public void simulateConcurrentPurchases(Long inventoryId) {
        CompletableFuture<Void> buyer1 = CompletableFuture.runAsync(()
                -> attemptPurchase("Buyer-1", inventoryId, 2)
        );

        CompletableFuture<Void> buyer2 = CompletableFuture.runAsync(()
                -> attemptPurchase("Buyer-2", inventoryId, 2)
        );

        // wait for both tasks to finish
        CompletableFuture.allOf(buyer1, buyer2).join();
    }

    private void attemptPurchase(String buyerName, Long inventoryId, int amount) {
        try {
            inventoryService.deductStock(inventoryId, amount, 1000);
            log.info("{} successfully purchased {} books.", buyerName, amount);
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.warn("{} failed due to concurrent update (Optimistic Lock triggered)!", buyerName);
        } catch (CompletionException ex) {
            if (ex.getCause() instanceof ObjectOptimisticLockingFailureException) {
                log.warn("{} failed due to concurrent update (Optimistic Lock triggered)!", buyerName);
            } else {
                throw ex;
            }
        }
    }
}
