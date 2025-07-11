package com.vitrine_freelancers_server.controllers.auth.requests;

public record LoginRequest(
        String email,
        String password
) {
}
