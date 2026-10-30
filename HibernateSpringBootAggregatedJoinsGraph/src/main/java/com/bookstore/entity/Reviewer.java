package com.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Reviewer implements Serializable {
    
    @Id    
    private Long id;
    
    @JsonBackReference
    @ManyToMany(mappedBy = "reviewers")
    private List<Review> reviews = new ArrayList<>();
    
    public String reviewerName;
    public Integer reviewerAge;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Integer getReviewerAge() {
        return reviewerAge;
    }

    public void setReviewerAge(Integer reviewerAge) {
        this.reviewerAge = reviewerAge;
    }
    
    @Override
    public boolean equals(Object obj) {              
        
        if (this == obj) {
            return true;
        }
        
        if (obj == null) {
            return false;
        }
        
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        return id != null && id.equals(((Reviewer) obj).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Reviewer{" + "id=" + id + ", reviewerName=" + reviewerName + ", reviewerAge=" + reviewerAge + '}';
    }   
}
