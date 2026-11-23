package com.flashsale.service;

import com.flashsale.entity.Product;
import org.springframework.stereotype.Service;
import com.flashsale.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final ProductRepository productRepository;

    public OrderService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void purchaseProduct(Long productId, int quantity) {
        
        // acquire exclusive lock BEFORE reading stock
        // thread B will pause right here until Thread A commits
        Product product = productRepository.findByIdForUpdate(productId).orElseThrow();

        // check inventory safely (no other thread can modify stock right now)
        if (product.getStock() < quantity) {
            throw new IllegalStateException("Out of stock!");
        }

        // deduct inventory (flushed on commit)
        product.setStock(product.getStock() - quantity);
    }
}
