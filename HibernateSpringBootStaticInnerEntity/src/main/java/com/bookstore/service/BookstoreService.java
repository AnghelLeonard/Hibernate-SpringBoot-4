package com.bookstore.service;

import com.bookstore.entity.Order;
import org.springframework.stereotype.Service;
import com.bookstore.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private final OrderRepository orderRepository;

    public BookstoreService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    
    @Transactional
    public Long insertOrderWithBookLine(String customerName, String isbn, int quantity, double price) {
                
        Order newOrder = new Order();
        newOrder.setCustomerName(customerName);
        
        // add the book line inside the aggregate boundary
        newOrder.addBook(isbn, quantity, price);
                
        Order savedOrder = orderRepository.save(newOrder);
        
        return savedOrder.getId();
    }
    
    @Transactional
    public void addBookToExistingOrder(Long orderId, String isbn, int quantity, double price) {
     
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.addBook(isbn, quantity, price);  
    }    
    
    @Transactional
    public void removeBookFromOrder(Long orderId, String isbn) {
        
        Order order = orderRepository.findById(orderId).orElseThrow();
        
        // removes the child instance from the parent collection
        order.removeBook(isbn);        
    }
    
     @Transactional
    public void removeWholeOrder(Long orderId) {        
        orderRepository.deleteById(orderId);
    }
    
    public Order fetchOrderWithBooks(Long orderId) {
        
        // fetching the aggregate root (Order)
        Order order = orderRepository.fetchOrderWithBooks(orderId);
        
        System.out.println(order.getLines());        
        System.out.println("Order Total: " + order.getTotalAmount());

        return order;
    }
}
