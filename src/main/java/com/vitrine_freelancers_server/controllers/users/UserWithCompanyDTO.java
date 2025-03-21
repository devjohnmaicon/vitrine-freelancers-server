package com.vitrine_freelancers_server.controllers.users;

public record UserWithCompanyDTO(
        Long id,
        String name,
        String email,
        String role,
        Long companyId,
        String companyName
) {
}
