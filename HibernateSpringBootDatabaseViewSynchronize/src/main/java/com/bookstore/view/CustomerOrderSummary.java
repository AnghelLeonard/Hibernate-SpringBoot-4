package com.bookstore.view;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Synchronize;

/*
@View(query = """
    SELECT o.customer_id as id, 
           COUNT(o.id) as total_orders, 
           SUM(o.amount) as total_spent 
    FROM orders o 
    GROUP BY o.customer_id
    """)
*/
@Entity
@Immutable
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "customer-order-summary") 
@Synchronize(value = "orders")
public class CustomerOrderSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column(name = "total_orders")
    private long totalOrders;

    @Column(name = "total_spent")
    private BigDecimal totalSpent;

    public Long getId() {
        return id;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    @Override
    public String toString() {
        return "CustomerOrderSummary{" + "id=" + id + ", totalOrders=" + totalOrders
                + ", totalSpent=" + totalSpent + '}';
    }
}
