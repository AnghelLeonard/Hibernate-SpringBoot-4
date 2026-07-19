package com.bookstore.forum.entity;

import com.bookstore.forum.converter.TsidAttributeConverter;
import io.hypersistence.tsid.TSID;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_details")
public class PostDetails {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Post post;

    @Column(name = "created_on")
    private LocalDateTime createdOn = LocalDateTime.now();

    @Column(name = "created_by")
    private String createdBy;

    @Tsid
    @Column(name = "external_id", length = 13)
    private String externalId;

    @Tsid
    @Convert(converter = TsidAttributeConverter.class)
    @Column(name = "public_id")
    private TSID publicId;

    public PostDetails() {
    }

    public PostDetails(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getExternalId() {
        return externalId;
    }

    public TSID getPublicId() {
        return publicId;
    }
}
