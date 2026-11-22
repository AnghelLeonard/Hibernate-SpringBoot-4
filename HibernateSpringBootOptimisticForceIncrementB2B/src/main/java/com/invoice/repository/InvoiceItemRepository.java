package com.invoice.repository;

import com.invoice.entity.InvoiceItem;
import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {

    @Query("SELECT COALESCE(SUM(item.amount), 0) FROM InvoiceItem item WHERE item.invoice.id = :invoiceId")
    BigDecimal sumAmountByInvoiceId(@Param("invoiceId") Long invoiceId);
}
