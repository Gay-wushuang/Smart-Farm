package com.wisdom.farm.common;

public final class UserContext {
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();
    private static final ThreadLocal<Boolean> CURRENT_ADMIN = ThreadLocal.withInitial(() -> false);

    private UserContext() {
    }

    public static void setUserId(Long userId) {
        CURRENT_USER_ID.set(userId);
    }

    public static void setAdmin(boolean admin) {
        CURRENT_ADMIN.set(admin);
    }

    public static Long getUserId() {
        return CURRENT_USER_ID.get();
    }

    public static Long requireUserId() {
        Long userId = getUserId();
        if (userId == null) {
            throw new IllegalStateException("用户未登录");
        }
        return userId;
    }

    public static boolean isAdmin() {
        return Boolean.TRUE.equals(CURRENT_ADMIN.get());
    }

    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new SecurityException("无管理员权限");
        }
    }

    public static void clear() {
        CURRENT_USER_ID.remove();
        CURRENT_ADMIN.remove();
    }
}
