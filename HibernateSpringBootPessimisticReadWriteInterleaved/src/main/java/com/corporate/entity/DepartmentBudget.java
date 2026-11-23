package com.corporate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class DepartmentBudget implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    private String deptCode;
    
    private BigDecimal maxTransferLimit;

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public BigDecimal getMaxTransferLimit() {
        return maxTransferLimit;
    }

    public void setMaxTransferLimit(BigDecimal maxTransferLimit) {
        this.maxTransferLimit = maxTransferLimit;
    }

    @Override
    public String toString() {
        return "DepartmentBudget{" + "deptCode=" + deptCode + ", maxTransferLimit=" + maxTransferLimit + '}';
    }   
}
