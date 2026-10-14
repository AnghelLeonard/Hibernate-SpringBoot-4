package com.bookstore.resolver;

public class SoftDeleteContext {
    private static final ThreadLocal<Boolean> INCLUDE_DELETED = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> INCLUDE_ONLY_DELETED = ThreadLocal.withInitial(() -> false);
    private static final ThreadLocal<Boolean> RESTORE_DELETED = ThreadLocal.withInitial(() -> false);

    public static boolean shouldShowDeleted() {
        return INCLUDE_DELETED.get();
    }

    public static void setShowDeleted(boolean showDeleted) {
        INCLUDE_DELETED.set(showDeleted);
    }

    public static void clear() {
        INCLUDE_DELETED.remove();
    }
    
    public static boolean shouldShowOnlyDeleted() {
        return INCLUDE_ONLY_DELETED.get();
    }

    public static void setShowOnlyDeleted(boolean showDeleted) {
        INCLUDE_ONLY_DELETED.set(showDeleted);
    }

    public static void clearOnlyDeleted() {
        INCLUDE_ONLY_DELETED.remove();
    }
    
    public static boolean shouldRestoreDeleted() {
        return RESTORE_DELETED.get();
    }

    public static void setRestoreDeleted(boolean showDeleted) {
        RESTORE_DELETED.set(showDeleted);
    }

    public static void clearRestoreDeleted() {
        RESTORE_DELETED.remove();
    }
}
