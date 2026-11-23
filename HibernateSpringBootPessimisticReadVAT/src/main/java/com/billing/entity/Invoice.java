package com.billing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private Long id;

    private String countryCode;
    private BigDecimal netAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public String toString() {
        return "Invoice{" + "id=" + id + ", countryCode=" + countryCode 
                + ", netAmount=" + netAmount + ", taxAmount=" + taxAmount 
                + ", totalAmount=" + totalAmount + '}';
    }        
}
