package com.bookstore.resolver;

import java.util.function.Supplier;

public class SoftDeleteRestoreDeletedParamResolver implements Supplier<Boolean> {
    
    @Override
    public Boolean get() {
        // maps directly to the :restoreDeleted SQL parameter
        return SoftDeleteContext.shouldRestoreDeleted();
    }
}
