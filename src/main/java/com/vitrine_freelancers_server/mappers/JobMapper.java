package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobUpdateRequest;
import com.vitrine_freelancers_server.controllers.jobs.response.JobReponse;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;

import java.util.List;

public class JobMapper {
    public static JobEntity toEntity(JobUpdateRequest jobUpdateRequest, CompanyEntity company) {
        return JobEntity.builder()
                .type(jobUpdateRequest.type())
                .position(jobUpdateRequest.position())
                .description(jobUpdateRequest.description())
                .date(jobUpdateRequest.date())
                .startTime(jobUpdateRequest.startTime())
                .endTime(jobUpdateRequest.endTime())
                .dailyValue(jobUpdateRequest.dailyValue())
                .requirements(jobUpdateRequest.requirements())
                .company(company)
                .open(true)
                .build();
    }

    public static JobUpdateRequest toDto(JobEntity jobEntity) {
        return new JobUpdateRequest(
                jobEntity.getType(),
                jobEntity.getPosition(),
                jobEntity.getDescription(),
                jobEntity.getDate(),
                jobEntity.getStartTime(),
                jobEntity.getEndTime(),
                jobEntity.getDailyValue(),
                jobEntity.getRequirements(),
                jobEntity.getCompany().getId()
        );
    }

    public static JobReponse toResponse(JobEntity job) {
        return new JobReponse(
                job.getId(),
                job.getType(),
                job.getPosition(),
                job.getDescription(),
                job.getDate(),
                job.getStartTime(),
                job.getEndTime(),
                job.getDailyValue(),
                job.getRequirements(),
                job.getOpen(),
                job.getCompany().getId(),
                job.getCompany().getName()
        );
    }

    public static List<JobReponse> toResponse(List<JobEntity> jobs) {
        return jobs.stream().map(JobMapper::toResponse).toList();
    }
}
