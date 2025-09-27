package com.driving_school_hibernate.dto;

public enum Roles {

    ADMIN,
    USER;






























    public static Roles fromString(String role) {
        if (role == null) return null;
        try {
            return Roles.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}