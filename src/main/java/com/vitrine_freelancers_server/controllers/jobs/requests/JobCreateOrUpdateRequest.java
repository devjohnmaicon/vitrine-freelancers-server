package com.vitrine_freelancers_server.controllers.jobs.requests;

import com.vitrine_freelancers_server.enums.JobType;

public record JobCreateOrUpdateRequest(
        JobType type,
        String position,
        String description,
        String date,
        String startTime,
        String endTime,
        Double dailyValue,
        String requirements
) {
}

