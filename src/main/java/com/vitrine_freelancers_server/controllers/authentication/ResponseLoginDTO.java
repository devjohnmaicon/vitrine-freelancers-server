package com.vitrine_freelancers_server.controllers.authentication;

public record ResponseLoginDTO(
        String name,
        Long companyId,
        String token
) {
}
