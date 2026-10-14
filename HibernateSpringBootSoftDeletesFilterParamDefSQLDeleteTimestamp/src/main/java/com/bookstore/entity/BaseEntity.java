package com.bookstore.entity;

import com.bookstore.resolver.SoftDeleteOnlyDeletedParamResolver;
import com.bookstore.resolver.SoftDeleteParamResolver;
import com.bookstore.resolver.SoftDeleteRestoreDeletedParamResolver;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@MappedSuperclass
@FilterDef(name = "includeExcludeDeleted", autoEnabled = true,
        parameters = {
            @ParamDef(
                    name = "includeDeleted",
                    type = Boolean.class,
                    resolver = SoftDeleteParamResolver.class
            ),
            @ParamDef(
                    name = "includeOnlyDeleted",
                    type = Boolean.class,
                    resolver = SoftDeleteOnlyDeletedParamResolver.class
            ),
            @ParamDef(
                    name = "restoreDeleted",
                    type = Boolean.class,
                    resolver = SoftDeleteRestoreDeletedParamResolver.class
            )}
)
public abstract class BaseEntity {

    @Column(name = "deleted")
    protected Instant deleted;

    public Instant getDeleted() {
        return deleted;
    }

    public void setDeleted(Instant deleted) {
        this.deleted = deleted;
    }
    
    public boolean isDeleted() {
        return deleted != null;
    }
}
