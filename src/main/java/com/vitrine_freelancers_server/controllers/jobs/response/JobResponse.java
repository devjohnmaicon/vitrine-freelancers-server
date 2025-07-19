package com.vitrine_freelancers_server.controllers.jobs.response;

import com.vitrine_freelancers_server.enums.JobType;

import java.time.LocalDateTime;

public record JobResponse(
        Long id,
        JobType type,
        String position,
        String description,
        String date,
        String startTime,
        String endTime,
        Double dailyValue,
        String requirements,
        Boolean open,
        LocalDateTime openUntil,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long companyId,
        String companyName
) {

}
