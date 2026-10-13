package com.bookstore.resolver;

import java.util.function.Supplier;

public class SoftDeleteParamResolver implements Supplier<Boolean> {
    
    @Override
    public Boolean get() {
        // maps directly to the :includeDeleted SQL parameter
        return SoftDeleteContext.shouldShowDeleted();
    }
}
