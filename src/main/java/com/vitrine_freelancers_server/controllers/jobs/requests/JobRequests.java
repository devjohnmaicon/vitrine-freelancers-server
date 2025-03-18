package com.vitrine_freelancers_server.controllers.jobs.requests;

public record JobRequests(
        String type,
        String description,
        String date,
        String startTime,
        String endTime,
        Double dailyValue,
        String requirements,
        Long company_id
) {
}

