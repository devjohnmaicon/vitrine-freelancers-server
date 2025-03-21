package com.vitrine_freelancers_server.controllers.authentication;

public record ResponseTokenDTO(
        String email,
        String company,
        String token
) {
}
