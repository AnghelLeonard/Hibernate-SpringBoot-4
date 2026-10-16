package com.bookstore.repository;

import com.bookstore.view.CustomerOrderSummary;
import jakarta.persistence.QueryHint;
import java.util.List;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CustomerOrderSummaryRepository extends JpaRepository<CustomerOrderSummary, Long> {   
    
    @QueryHints(@QueryHint(name = HibernateHints.HINT_CACHEABLE, value = "true"))
    List<CustomerOrderSummary> findByTotalOrdersGreaterThanEqual(long minOrders);
}
