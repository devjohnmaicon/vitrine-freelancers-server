package com.vitrine_freelancers_server.enums;

public enum UserRole {
    COMPANY("company"),
    ADMIN("admin");
    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
