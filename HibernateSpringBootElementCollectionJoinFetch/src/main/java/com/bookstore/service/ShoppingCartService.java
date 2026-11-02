package com.bookstore.service;

import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.entity.ShoppingCart;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }        
        
    public ShoppingCart allShoppingCart() {
        
        return shoppingCartRepository.fetchShoppingCart();
    }
        
    public ShoppingCart byPriceShoppingCart() {
        
        return shoppingCartRepository.fetchShoppingCartByPrice(40);
    }
}
