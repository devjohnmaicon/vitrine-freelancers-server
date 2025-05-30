package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobUpdateRequest;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.JobNotFoundException;
import com.vitrine_freelancers_server.exceptions.UserNotAuthorizationException;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;


    @PreAuthorize("hasRole('ROLE_COMPANY') or hasRole('ROLE_COMPANY')")
    public JobEntity createJob(JobUpdateRequest request) {
        CompanyEntity company = companyService.companyById(request.company_id());
        return jobRepository.save(JobMapper.toEntity(request, company));
    }

    public Page<JobEntity> findJobsOpen(Pageable pageable) {
        return jobRepository.findJobEntitiesByOpenIsTrue(pageable);
    }

    public JobEntity findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job " + id + " not found"));
    }

    public JobEntity updateJob(Long jobId, JobUpdateRequest requestUpdate, UserEntity user) {
        JobEntity job = validateUserAndJob(jobId, user);
        job.setType(requestUpdate.type());
        job.setPosition(requestUpdate.position());
        job.setDescription(requestUpdate.description());
        job.setDate(requestUpdate.date());
        job.setStartTime(requestUpdate.startTime());
        job.setEndTime(requestUpdate.endTime());
        job.setDailyValue(requestUpdate.dailyValue());
        job.setRequirements(requestUpdate.requirements());
        return jobRepository.save(job);
    }

    public void closeJob(Long id, UserEntity user) {
        JobEntity job = validateUserAndJob(id, user);
        job.setOpen(false);
        jobRepository.save(job);
    }

    public List<JobEntity> findJobsByCompany(UserEntity user) {
        CompanyEntity company = companyService.findCompanyByUser(user);
        return jobRepository.findJobEntitiesByCompanyId(company.getId());
    }

    public List<JobEntity> findLastThreeJobs() {
        return jobRepository.findTop3ByOpenIsTrueOrderByCreatedAtDesc();
    }

    public JobEntity validateUserAndJob(Long id, UserEntity user) {
        CompanyEntity company = companyService.findCompanyByUser(user);
        JobEntity job = jobRepository.findJobEntityByIdAndOpenIsTrue(id).orElseThrow(() -> new JobNotFoundException("O job " + id + " não existe ou já foi fechado"));
        if (!job.getCompany().getId().equals(company.getId())) throw new UserNotAuthorizationException();
        return job;
    }
}