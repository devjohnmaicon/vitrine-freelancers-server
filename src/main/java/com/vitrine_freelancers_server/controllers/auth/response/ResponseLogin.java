package com.vitrine_freelancers_server.controllers.auth.response;

public record ResponseLogin(
        String name,
        Long companyId,
        String token
) {
}
