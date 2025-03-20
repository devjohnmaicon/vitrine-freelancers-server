package com.vitrine_freelancers_server.controllers.authentication;

public record LoginRequestDTO(
        String email,
        String password
) {
}
