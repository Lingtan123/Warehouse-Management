package com.wms.auth;

//辅助类，目的是存储当前操作人员
public final class UserContext {
    //SpringBoot自带Tomcat，Tomcat内置线程池，无需手动创建
    //拿到线程池中处理当前的连接的线程的线程号，把信息放在这个线程中，防止其它线程获取
    private static final ThreadLocal<CurrentUser> USER_HOLDER = new ThreadLocal<>();

    private UserContext() {
    }

    public static void setCurrentUser(CurrentUser currentUser) {
        USER_HOLDER.set(currentUser);
    }

    public static CurrentUser getCurrentUser() {
        return USER_HOLDER.get();
    }

    public static void clear() {
        USER_HOLDER.remove();
    }
}
