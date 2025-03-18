package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;

public class JobMapper {
    public static JobEntity toEntity(JobRequests jobRequests, CompanyEntity company) {
        return JobEntity.builder()
                .type(jobRequests.type())
                .description(jobRequests.description())
                .date(jobRequests.date())
                .startTime(jobRequests.startTime())
                .endTime(jobRequests.endTime())
                .dailyValue(jobRequests.dailyValue())
                .requirements(jobRequests.requirements())
                .company(company)
                .open(true)
                .build();
    }

    public static JobRequests toDto(JobEntity jobEntity) {
        return new JobRequests(
                jobEntity.getType(),
                jobEntity.getDescription(),
                jobEntity.getDate(),
                jobEntity.getStartTime(),
                jobEntity.getEndTime(),
                jobEntity.getDailyValue(),
                jobEntity.getRequirements(),
                jobEntity.getCompany().getId()
        );
    }
}
