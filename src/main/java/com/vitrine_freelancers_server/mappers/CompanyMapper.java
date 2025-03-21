package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.controllers.companies.response.CompanyResponse;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;

import java.util.List;

public class CompanyMapper {
    public static CompanyEntity toEntity(CreateCompanyDTO request, UserEntity user) {
        return CompanyEntity.builder()
                .name(request.name())
                .isActive(true)
                .user(user)
                .build();
    }

    public static CompanyResponse toResponse(CompanyEntity company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getIsActive(),
                company.getCreatedAt(),
                company.getUpdatedAt(),
                company.getUser().getId(),
                company.getUser().getName()
        );
    }

    public static List<CompanyResponse> toResponse(List<CompanyEntity> companies) {
        return companies.stream().map(CompanyMapper::toResponse).toList();
    }

}
