package com.bookstore.repository;

import com.bookstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // fetching the order along with its books in a single database roundtrip (JOIN FETCH)
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.lines WHERE o.id = :id")
    Order fetchOrderWithBooks(@Param("id") Long id);
}
