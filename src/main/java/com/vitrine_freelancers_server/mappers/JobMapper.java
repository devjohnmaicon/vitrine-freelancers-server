package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.controllers.jobs.JobRequests;

public class JobMapper {
    public static JobEntity toEntity(JobRequests jobRequests) {
        return JobEntity.builder()
                .type(jobRequests.type())
                .description(jobRequests.description())
                .date(jobRequests.date())
                .startTime(jobRequests.startTime())
                .endTime(jobRequests.endTime())
                .dailyValue(jobRequests.dailyValue())
                .requirements(jobRequests.requirements())
                .companyId(jobRequests.companyId())
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
                jobEntity.getCompanyId()
        );
    }
}
