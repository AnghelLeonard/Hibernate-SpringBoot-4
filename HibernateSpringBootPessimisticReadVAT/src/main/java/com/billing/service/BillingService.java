package com.billing.service;

import com.billing.entity.Invoice;
import com.billing.entity.TaxRate;
import com.billing.repository.InvoiceRepository;
import org.springframework.stereotype.Service;
import com.billing.repository.TaxRateRepository;
import java.math.BigDecimal;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

    private final TaxRateRepository taxRateRepository;
    private final InvoiceRepository invoiceRepository;

    public BillingService(TaxRateRepository taxRateRepository, 
                          InvoiceRepository invoiceRepository) {
        this.taxRateRepository = taxRateRepository;
        this.invoiceRepository = invoiceRepository;
    }

    // Hundreds of threads can execute this concurrently without blocking each other.
    // Guaranteed that the tax rate will NOT change mid-transaction.     
    @Transactional
    public Invoice generateInvoice(Long invoiceId, String countryCode, BigDecimal netAmount) {
        
        // acquire PESSIMISTIC_READ on the tax rate
        TaxRate taxRate = taxRateRepository.findWithSharedLock(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown country"));

        // simulate complex billing/discount rules using this rate
        // (during this window, no admin can alter the rate underneath us)
        BigDecimal tax = netAmount.multiply(taxRate.getPercentage());
        BigDecimal total = netAmount.add(tax);

        // persist the invoice (only Invoice gets an exclusive write lock, not TaxRate)
        Invoice invoice = new Invoice();
        invoice.setId(invoiceId);
        invoice.setCountryCode(countryCode);
        invoice.setNetAmount(netAmount);
        invoice.setTaxAmount(tax);
        invoice.setTotalAmount(total);

        return invoiceRepository.save(invoice);
    }
    
     // rare admin writes - waits until all in-flight generateInvoice() transactions
     // release their PESSIMISTIC_READ locks before updating the rate.    
    @Transactional
    public void updateTaxRate(String countryCode, BigDecimal newPercentage) {
        
        TaxRate taxRate = taxRateRepository.findById(countryCode)
                .orElseThrow(() -> new IllegalArgumentException("Unknown country"));
        
        taxRate.setPercentage(newPercentage);
    }
}