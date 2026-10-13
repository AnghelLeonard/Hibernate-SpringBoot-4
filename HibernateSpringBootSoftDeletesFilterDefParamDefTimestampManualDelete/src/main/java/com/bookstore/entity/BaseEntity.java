package com.bookstore.entity;

import com.bookstore.resolver.SoftDeleteParamResolver;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(
        name = "deletedFilter",
        autoEnabled = true,
        parameters = @ParamDef(
                name = "includeDeleted",
                type = Boolean.class,
                resolver = SoftDeleteParamResolver.class)
)
public abstract class BaseEntity {

    @Column(name = "deleted_at")
    private Instant deletedAt;

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
