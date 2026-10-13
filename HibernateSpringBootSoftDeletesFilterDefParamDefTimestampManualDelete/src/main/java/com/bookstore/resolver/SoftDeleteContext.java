package com.bookstore.resolver;

public class SoftDeleteContext {
    private static final ThreadLocal<Boolean> INCLUDE_DELETED = ThreadLocal.withInitial(() -> false);

    public static boolean shouldShowDeleted() {
        return INCLUDE_DELETED.get();
    }

    public static void setShowDeleted(boolean showDeleted) {
        INCLUDE_DELETED.set(showDeleted);
    }

    public static void clear() {
        INCLUDE_DELETED.remove();
    }
}
