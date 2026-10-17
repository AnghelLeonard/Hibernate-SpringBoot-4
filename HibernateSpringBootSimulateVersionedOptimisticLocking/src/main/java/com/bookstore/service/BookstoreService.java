package com.bookstore.service;

import com.bookstore.entity.Inventory;
import com.bookstore.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService implements Runnable {

    private final InventoryRepository inventoryRepository;

    public BookstoreService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public void run() {

        Inventory inventory = inventoryRepository.findById(1L).orElseThrow();
        try {
            Thread.sleep(1000); // this is added just to ensure that both transactions are here
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        int quantity = inventory.getQuantity();
        if ((quantity - 2) >= 0) { // of course, it can be any quantity, 2 is just an example
            inventory.setQuantity(inventory.getQuantity() - 2);
        } else {
            System.out.println("Notify the client (" + Thread.currentThread().getName()
                    + ") that there are no more books available ...");
        }
    }
}
