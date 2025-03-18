package com.vitrine_freelancers_server.controllers.jobs.response;

import com.vitrine_freelancers_server.enums.JobType;

public record JobReponse(
        Long id,
        JobType type,
        String description,
        String date,
        String startTime,
        String endTime,
        Double dailyValue,
        String requirements,
        Long companyId,
        String companyName
) {
}
