package com.bookstore.repository;

import com.bookstore.entity.Transaction;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    
    Window<Transaction> findFirst100ByStatusAndAmountGreaterThanOrderByProcessedAtDescIdDesc(
            String status, 
            Double amount, 
            ScrollPosition scrollPosition
    );
    
    // use any derived query method and pass the proper parameters
}
