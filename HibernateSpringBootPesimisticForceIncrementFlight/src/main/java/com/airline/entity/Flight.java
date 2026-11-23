package com.airline.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.io.Serializable;
import org.springframework.data.domain.Persistable;

@Entity
public class Flight implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id    
    private Long id;

    private String flightNumber;   
    private Integer capacity;

    @Version
    private Integer version;

     @Transient
    private boolean isNew = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getVersion() {
        return version;
    }        

    @Override
    public String toString() {
        return "Flight{" + "id=" + id + ", flightNumber=" + flightNumber 
                + ", capacity=" + capacity + ", version=" + version + '}';
    }   
}
