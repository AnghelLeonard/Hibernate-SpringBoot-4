package com.billing.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class TaxRate implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String countryCode; // e.g., "RO"
    
    private BigDecimal percentage; // e.g., 0.19

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "TaxRate{" + "countryCode=" + countryCode + ", percentage=" + percentage + '}';
    }        
}
