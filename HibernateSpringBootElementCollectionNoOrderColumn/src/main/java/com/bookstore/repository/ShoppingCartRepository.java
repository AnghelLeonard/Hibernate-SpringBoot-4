package com.bookstore.repository;

import com.bookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly=true)
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {  
        
    ShoppingCart findByOwner(String owner);
}

