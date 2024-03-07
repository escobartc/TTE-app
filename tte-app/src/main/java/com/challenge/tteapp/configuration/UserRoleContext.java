package com.challenge.tteapp.configuration;



public class UserRoleContext {

    private static final ThreadLocal<String> userRole = new ThreadLocal<>();

    public static String getRole() {
        return userRole.get();
    }

    public static void setRole(String role) {
        userRole.set(role);
    }

    public static void clear() {
        userRole.remove();
    }
}