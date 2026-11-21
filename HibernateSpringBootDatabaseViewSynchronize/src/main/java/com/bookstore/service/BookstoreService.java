package com.bookstore.service;

import com.bookstore.entity.Order;
import com.bookstore.repository.CustomerOrderSummaryRepository;
import com.bookstore.view.CustomerOrderSummary;
import org.springframework.stereotype.Service;
import com.bookstore.repository.OrderRepository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookstoreService {

    private static final Logger log = LoggerFactory.getLogger(BookstoreService.class);

    private final OrderRepository orderRepository;
    private final CustomerOrderSummaryRepository summaryRepository;

    public BookstoreService(OrderRepository orderRepository,
            CustomerOrderSummaryRepository summaryRepository) {
        this.orderRepository = orderRepository;
        this.summaryRepository = summaryRepository;
    }

    // warm up the query cache (database is empty)
    public void warmUpCache() {
        
        log.info("--- Fetching first time (database is empty) ---");
        
        // SQL: select cos1_0.id,cos1_0.total_orders,cos1_0.total_spent 
        // from customer_order_summary cos1_0 where cos1_0.total_orders>=?
        List<CustomerOrderSummary> results = summaryRepository.findByTotalOrdersGreaterThanEqual(0);
        
        log.info("Found summaries: {}", results.size()); // 0
    }

    // add an order and read again
    @Transactional
    public void modifyDataAndRead(Long customerId) {
        
        log.info("--- Saving a new order ---");
        
        Order order = new Order();
        order.setId(1L);
        order.setCustomerId(customerId);
        order.setAmount(new BigDecimal("150.00"));
        order.setCreatedAt(Instant.now());
        
        orderRepository.save(order);

        log.info("--- Fetching second time ---");
        List<CustomerOrderSummary> results = summaryRepository.findByTotalOrdersGreaterThanEqual(0);
        
        log.info("Found summaries on second try: {}", results.size());
    }        
    
    // read again, no sync needed
    public void readDataFromL2() {
        
        log.info("--- Fetching from L2 ---");
        
        // SQL: select cos1_0.id,cos1_0.total_orders,cos1_0.total_spent 
        // from customer_order_summary cos1_0 where cos1_0.total_orders>=?
        List<CustomerOrderSummary> results = summaryRepository.findByTotalOrdersGreaterThanEqual(0);
        
        log.info("Found summaries on third try: {}", results.size()); // 1
    }
}
