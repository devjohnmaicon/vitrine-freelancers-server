package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.controllers.companies.requests.CompanyRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;

public class CompanyMapper {
    public static CompanyEntity toEntity(CompanyRequests request, UserEntity user) {
        return CompanyEntity.builder()
                .name(request.name())
                .user(user)
                .build();
    }
}
