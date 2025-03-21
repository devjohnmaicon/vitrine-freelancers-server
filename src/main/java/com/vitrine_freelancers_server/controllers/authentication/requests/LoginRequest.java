package com.vitrine_freelancers_server.controllers.authentication.requests;

public record LoginRequest(
        String email,
        String password
) {
}
