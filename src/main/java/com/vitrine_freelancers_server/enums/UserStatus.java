package com.vitrine_freelancers_server.enums;

public enum UserStatus {
    INACTIVE("inactive"),
    ACTIVE("active"),
    BLOCKED("blocked");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
