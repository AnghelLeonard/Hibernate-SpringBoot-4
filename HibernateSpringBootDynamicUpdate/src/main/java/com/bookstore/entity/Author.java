package com.bookstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.type.SqlTypes;

@Entity
@DynamicUpdate
@OptimisticLocking(type = OptimisticLockType.DIRTY)
public class Author implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id    
    private Long id;
   
    private String name;
    private String email;
    private String primaryGenre;
    private String countryCode;
    private LocalDate birthDate;
    
    @Column(columnDefinition = "TEXT")
    private String biography;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "social_metadata", columnDefinition = "jsonb")
    private Map<String, Object> socialMetadata;
    
    private Integer sellRank;
    private Long totalBooksSold;
    private Instant lastActiveAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrimaryGenre() {
        return primaryGenre;
    }

    public void setPrimaryGenre(String primaryGenre) {
        this.primaryGenre = primaryGenre;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public Map<String, Object> getSocialMetadata() {
        return socialMetadata;
    }

    public void setSocialMetadata(Map<String, Object> socialMetadata) {
        this.socialMetadata = socialMetadata;
    }

    public Integer getSellRank() {
        return sellRank;
    }

    public void setSellRank(Integer sellRank) {
        this.sellRank = sellRank;
    }

    public Long getTotalBooksSold() {
        return totalBooksSold;
    }

    public void setTotalBooksSold(Long totalBooksSold) {
        this.totalBooksSold = totalBooksSold;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public void setLastActiveAt(Instant lastActiveAt) {
        this.lastActiveAt = lastActiveAt;
    }

    @Override
    public String toString() {
        return "Author{" + "id=" + id + ", name=" + name + ", email=" + email 
                + ", primaryGenre=" + primaryGenre + ", countryCode=" + countryCode 
                + ", birthDate=" + birthDate + ", biography=" + biography + ", socialMetadata=" 
                + socialMetadata + ", sellRank=" + sellRank + ", totalBooksSold=" 
                + totalBooksSold + ", lastActiveAt=" + lastActiveAt + '}';
    }        
}
