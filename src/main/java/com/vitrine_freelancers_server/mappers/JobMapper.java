package com.vitrine_freelancers_server.mappers;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobCreateOrUpdateRequest;
import com.vitrine_freelancers_server.controllers.jobs.response.JobResponse;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;

import java.time.LocalDateTime;
import java.util.List;

public class JobMapper {
    public static JobEntity toEntity(JobCreateOrUpdateRequest jobUpdateRequest, CompanyEntity company) {
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
                .openUntil(LocalDateTime.now().plusHours(jobUpdateRequest.openForHours() != null ? jobUpdateRequest.openForHours() : 12).withMinute(0).withSecond(0).withNano(0))
                .build();
    }

    public static JobResponse toResponse(JobEntity job) {
        return new JobResponse(
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
                job.getOpenUntil(),
                job.getCreatedAt(),
                job.getUpdatedAt(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getApplicationsCount() != null ? job.getApplicationsCount() : 0,
                job.getHasNewApplications() != null && job.getHasNewApplications()
        );
    }

    public static List<JobResponse> toResponse(List<JobEntity> jobs) {
        return jobs.stream().map(JobMapper::toResponse).toList();
    }
}
