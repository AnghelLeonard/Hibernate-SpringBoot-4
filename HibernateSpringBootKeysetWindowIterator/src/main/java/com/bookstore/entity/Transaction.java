package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(
        name = "transaction",
        indexes = {
            @Index(name = "idx_tx_status_amount_processed_id",
                    columnList = "status, amount, processed_at DESC, id DESC")
        }
)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String status; // e.g. "COMPLETED", "PENDING"

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private Long userId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt;

    public Transaction() {
    }

    public Transaction(String status, Double amount, Long userId, Instant processedAt) {
        this.status = status;
        this.amount = amount;
        this.userId = userId;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id=" + id + ", status=" + status + ", amount=" + amount
                + ", userId=" + userId + ", processedAt=" + processedAt + '}';
    }
}
