package com.bookstore.resolver;

import java.util.function.Supplier;

public class SoftDeleteOnlyDeletedParamResolver implements Supplier<Boolean> {
    
    @Override
    public Boolean get() {
        // maps directly to the :includeOnlyDeleted SQL parameter
        return SoftDeleteContext.shouldShowOnlyDeleted();
    }
}