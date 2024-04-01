package com.challenge.tteapp.configuration;

public class InfoToken {
    private InfoToken() {
    }

    private static final ThreadLocal<String> userRole = new ThreadLocal<>();
    private static final ThreadLocal<String> name = new ThreadLocal<>();

    public static String getRole() {
        return userRole.get();
    }

    public static String getName() {
        return name.get();
    }

    public static void setRole(String role) {
        userRole.set(role);
    }

    public static void setName(String username) {
        name.set(username);
    }

    public static void clear() {
        userRole.remove();
        name.remove();
    }
}