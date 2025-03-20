package com.vitrine_freelancers_server.controllers.authentication;

public record RegisterRequestDTO(
        String email,
        String password,
        String name
) {
}
