package com.billing.repository;

import com.billing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
