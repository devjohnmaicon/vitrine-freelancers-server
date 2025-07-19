package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.dtos.job.JobProjection;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobSchedule {
    private static final Logger logger = LoggerFactory.getLogger(JobSchedule.class);
    private final JobRepository jobRepository;

    public JobSchedule(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(cron = "${spring.task.scheduling.expression}")
    @Transactional
    public void performScheduledTask() {
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0).withNano(0);
        List<JobProjection> listIdsJobs = jobRepository.findJobsByOpenIsTrueAndOpenUntilEquals(localDateTime);
        if (listIdsJobs.isEmpty()) {
            logger.info("Execution: " + localDateTime + " || No jobs found to close at ");
            return;
        }

        List<Long> ids = listIdsJobs.stream().map(JobProjection::getId).collect(Collectors.toList());
        logger.info("Execution: " + localDateTime + " || Found " + listIdsJobs.size() + " jobs to close; IDS - " + ids);

        jobRepository.closeJobsByIds(ids);
    }

}
