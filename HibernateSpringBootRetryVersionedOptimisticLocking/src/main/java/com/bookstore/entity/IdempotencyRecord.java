package com.bookstore.entity;

import jakarta.persistence.*;
import java.time.Duration;
import org.springframework.data.domain.Persistable;
import java.time.Instant;

@Entity
public class IdempotencyRecord implements Persistable<String> {

    @Id
    private String idempotencyKey;

    private String requestHash;
    private int responseStatus;
    private String responseBody;
    private Instant expiresAt;

    @Transient
    private boolean isNew = true;

    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }

    @Override
    public String getId() {
        return idempotencyKey;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostPersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public void setExpiresAt(Duration ttl) {
        this.expiresAt = Instant.now().plus(ttl);
    }       

    public String getRequestHash() {
        return requestHash;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setRequestHash(String requestHash) {
        this.requestHash = requestHash;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public String toString() {
        return "IdempotencyRecord{" + "requestHash=" + requestHash 
                + ", responseStatus=" + responseStatus + ", responseBody=" + responseBody + '}';
    }        
}
