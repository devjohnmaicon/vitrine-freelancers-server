package com.vitrine_freelancers_server.controllers.auth.response;

public record ResponseToken(
        String email,
        String company,
        String token
) {
}
