package com.corporate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class EmployeeWallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String employeeId;

    private String deptCode;
    private BigDecimal balance;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "EmployeeWallet{" + "employeeId=" + employeeId + ", deptCode=" + deptCode
                + ", balance=" + balance + '}';
    }
}
