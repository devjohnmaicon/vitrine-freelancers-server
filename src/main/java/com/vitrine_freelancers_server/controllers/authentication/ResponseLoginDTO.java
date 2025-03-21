package com.vitrine_freelancers_server.controllers.authentication;

public record ResponseLoginDTO(
        String email,
        String token
) {
}
