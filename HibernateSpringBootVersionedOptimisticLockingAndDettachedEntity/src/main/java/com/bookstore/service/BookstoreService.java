package com.bookstore.service;

import com.bookstore.entity.Inventory;
import com.bookstore.repository.InventoryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final InventoryRepository inventoryRepository;

    public BookstoreService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Inventory firstTransactionFetchesAndReturn() {
        Inventory firstInventory = inventoryRepository.findById(1L).orElseThrow();

        return firstInventory;
    }

    @Transactional
    public void secondTransactionFetchesAndReturn() {
        Inventory secondInventory = inventoryRepository.findById(1L).orElseThrow();

        int quantity = secondInventory.getQuantity() - 1;
        if ((quantity - 1) >= 0) {
            secondInventory.setQuantity(secondInventory.getQuantity() - 1);
        } else {
            System.out.println("Notify the client (" + Thread.currentThread().getName()
                    + ") that there are no more books available ...");
        }
    }

    public void thirdTransactionMergesAndUpdates(Inventory firstInventory) {
        inventoryRepository.save(firstInventory); // calls EntityManager#merge() behind the scene    

        // this ends up in optimistic locking exception
    }
}
