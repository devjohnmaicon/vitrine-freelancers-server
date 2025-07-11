package com.vitrine_freelancers_server.controllers.auth.requests;

import com.vitrine_freelancers_server.controllers.auth.dtos.CreateCompanyDTO;
import com.vitrine_freelancers_server.controllers.auth.dtos.CreateUserDTO;

public record RegisterUserCompanyDTO(
        CreateUserDTO user,
        CreateCompanyDTO company
) {
}
