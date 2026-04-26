package com.syc.salesAgent.security;

public class UserContext {

    //ThreadLocal 上下文
    private static final ThreadLocal<UserInfo> HOLDER = new ThreadLocal<>();

    public record UserInfo(Long userId, String username, String role, Long regionId, Long repId) {}

    public static void set(UserInfo info) { HOLDER.set(info); }
    public static UserInfo get() { return HOLDER.get(); }
    public static void clear() { HOLDER.remove(); }

    //是不是领导
    public static boolean isDirector() {
        UserInfo u = get();
        return u != null && "SALES_DIRECTOR".equals(u.role());
    }

    //是不是经理
    public static boolean isManager() {
        UserInfo u = get();
        return u != null && "SALES_MANAGER".equals(u.role());
    }
}