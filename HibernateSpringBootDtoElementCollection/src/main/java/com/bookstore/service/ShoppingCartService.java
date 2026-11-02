package com.bookstore.service;

import com.bookstore.repository.ShoppingCartRepository;
import com.bookstore.dto.ShoppingCartDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartService {
    
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }        
        
    public List<ShoppingCartDto> allShoppingCart() {
        
        return shoppingCartRepository.fetchShoppingCart();
    }
        
    public List<ShoppingCartDto> byPriceShoppingCart() {
        
        return shoppingCartRepository.fetchShoppingCartByPrice(40);
    }
}
