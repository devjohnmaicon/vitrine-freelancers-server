package com.vitrine_freelancers_server.controllers.users;

public record UserUpdateRequest(
        String name,
        String email,
        String password,
        String role
) {
}
