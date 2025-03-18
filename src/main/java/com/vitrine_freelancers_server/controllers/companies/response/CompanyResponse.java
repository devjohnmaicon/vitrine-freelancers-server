package com.vitrine_freelancers_server.controllers.companies.response;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        Boolean isActive,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long userId,
        String userName
) {
}
