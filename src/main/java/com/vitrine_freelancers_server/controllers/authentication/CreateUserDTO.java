package com.vitrine_freelancers_server.controllers.authentication;

import com.vitrine_freelancers_server.enums.UserRole;


public record CreateUserDTO(
        String email,
        String password,
        String name,
        UserRole role
) {
}
