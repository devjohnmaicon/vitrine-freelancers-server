package com.vitrine_freelancers_server.controllers.jobs;

public record JobRequests(
        String type,
        String description,
        String date,
        String startTime,
        String endTime,
        Double dailyValue,
        String requirements,
        Long companyId
) {

}
//        "type": "FREELANCER",
//        "category": "Motoboy",
//        "description": "job1 description",
//        "date": "2021-08-01",
//        "startTime": "10:00",
//        "endTime": "18:00",
//        "dailyValue": 100,
//        "requirements": "Moto própria",
//        "companyId": 1