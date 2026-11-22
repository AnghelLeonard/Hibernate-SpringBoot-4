package com.invoice.service;

import com.invoice.entity.Invoice;
import com.invoice.entity.InvoiceItem;
import org.springframework.stereotype.Service;
import com.invoice.repository.InvoiceItemRepository;
import com.invoice.repository.InvoiceRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository itemRepository;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceItemRepository itemRepository) {
        this.invoiceRepository = invoiceRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public InvoiceItem addItem(Long invoiceId, Long newItemId, BigDecimal itemPrice, String description) {
        
        // load parent with OPTIMISTIC_FORCE_INCREMENT
        Invoice invoice = invoiceRepository.findByIdWithForceIncrement(invoiceId).orElseThrow();

        // validate aggregate invariant across existing child items
        BigDecimal currentTotal = itemRepository.sumAmountByInvoiceId(invoiceId);
        if (currentTotal.add(itemPrice).compareTo(invoice.getCreditLimit()) > 0) {
            throw new IllegalStateException("Credit limit of " + invoice.getCreditLimit() + " exceeded");
        }

        // save child entity (invoice row itself is not modified)
        InvoiceItem newItem = new InvoiceItem();
        newItem.setId(newItemId);
        newItem.setAmount(itemPrice);
        newItem.setDescription(description);
        newItem.setInvoice(invoice);
        
        return itemRepository.save(newItem);
    }
}