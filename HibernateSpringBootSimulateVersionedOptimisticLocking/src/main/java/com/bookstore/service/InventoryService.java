package com.bookstore.service;

import com.bookstore.entity.Inventory;
import com.bookstore.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void deductStock(Long inventoryId, int amount, long delayMs) {
        Inventory inventory = inventoryRepository.findById(inventoryId).orElseThrow();

        if (delayMs > 0) {
            try {
                Thread.sleep(delayMs); // forces both threads to hold the same @Version before committing
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        if (inventory.getQuantity() >= amount) {
            inventory.setQuantity(inventory.getQuantity() - amount);
        } else {
            throw new IllegalStateException("Not enough stock available");
        }
    }
}
