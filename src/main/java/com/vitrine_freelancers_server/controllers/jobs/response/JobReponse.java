package com.vitrine_freelancers_server.controllers.jobs.response;

public record JobReponse(
        Long id,
        String type,
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
