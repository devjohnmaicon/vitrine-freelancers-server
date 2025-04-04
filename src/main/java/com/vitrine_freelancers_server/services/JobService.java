package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;


    public JobEntity createJob(JobRequests request) {
        CompanyEntity company = companyService.findCompanyById(request.company_id());
        return jobRepository.save(JobMapper.toEntity(request, company));
    }

    public List<JobEntity> findJobsOpen() {
        return jobRepository.findJobEntitiesByOpenIsTrueOrderByCreatedAtDesc();

    }

    public JobEntity findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public JobEntity updateJob(Long id, JobRequests requestUpdate) {
        JobEntity job = findJobById(id);
        job.setType(requestUpdate.type());
        job.setDescription(requestUpdate.description());
        job.setDate(requestUpdate.date());
        job.setStartTime(requestUpdate.startTime());
        job.setEndTime(requestUpdate.endTime());
        job.setDailyValue(requestUpdate.dailyValue());
        job.setRequirements(requestUpdate.requirements());
        return jobRepository.save(job);
    }


    public void closeJob(Long id) {
        JobEntity job = findJobById(id);
        job.setOpen(false);
        jobRepository.save(job);
    }

    public List<JobEntity> findJobsByCompany(Long id) {
        return jobRepository.findJobEntitiesByCompanyId(id);
    }
}