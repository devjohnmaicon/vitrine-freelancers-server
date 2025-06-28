package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobCreateOrUpdateRequest;
import com.vitrine_freelancers_server.controllers.jobs.response.JobResponse;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.dtos.User.UserPrincipalDTO;
import com.vitrine_freelancers_server.exceptions.JobNotFoundException;
import com.vitrine_freelancers_server.exceptions.UserNotAuthorizationException;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    private UserPrincipalDTO getUser() throws RuntimeException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipalDTO)) {
            throw new UserNotAuthorizationException("Usuário não autenticado ou não autorizado");
        }
        UserPrincipalDTO userPrincipal = (UserPrincipalDTO) authentication.getPrincipal();
        return userPrincipal;
    }

    public JobEntity createJob(JobCreateOrUpdateRequest request) {
        UserPrincipalDTO user = getUser();
        CompanyEntity company = companyService.companyById(user.getCompanyId());
        return jobRepository.save(JobMapper.toEntity(request, company));
    }

    public Page<JobEntity> findAllJobsOpen(Pageable pageable) {
        return jobRepository.findJobEntitiesByOpenIsTrue(pageable);
    }

    public JobEntity findJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new JobNotFoundException("Job " + id + " not found"));
    }

    public JobEntity findJobByIdOpen(Long id) {
        return jobRepository.findJobEntityByIdAndOpenIsTrue(id)
                .orElseThrow(() -> new JobNotFoundException("O job " + id + " não existe ou já foi fechado"));
    }

    public JobEntity updateJob(Long jobId, JobCreateOrUpdateRequest requestUpdate, UserEntity user) {
        JobEntity job = userCanExecute(jobId);
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

    public void closeJob(Long id) {
        JobEntity job = userCanExecute(id);
        job.setOpen(false);
        jobRepository.save(job);
    }

    public List<JobResponse> findJobsByCompanyId(Long companyId) {
        List<JobEntity> jobsList = jobRepository.findByCompanyId(companyId);
        return jobsList.stream()
                .map(JobMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<JobResponse> findByType(Long typeId) {
        List<JobEntity> jobs = jobRepository.findJobEntityByType(typeId);
        return jobs.stream()
                .map(JobMapper::toResponse)
                .collect(Collectors.toList());
    }

    public JobEntity userCanExecute(Long id) {
        UserPrincipalDTO user = getUser();
        JobEntity job = findJobByIdOpen(id);
        if (!job.getCompany().getId().equals(user.getCompanyId()))
            throw new UserNotAuthorizationException("Não autotizado a editar ou excluir esta vaga.");
        return job;
    }

}