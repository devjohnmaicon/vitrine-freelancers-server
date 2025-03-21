package com.vitrine_freelancers_server.controllers.authentication;

public record RegisterUserCompanyDTO(
        CreateUserDTO user,
        CreateCompanyDTO company
) {
}
