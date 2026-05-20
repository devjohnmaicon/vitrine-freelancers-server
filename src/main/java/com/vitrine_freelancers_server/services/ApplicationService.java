package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.domain.ApplicationEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.dtos.User.UserPrincipalDTO;
import com.vitrine_freelancers_server.enums.ApplicationStatus;
import com.vitrine_freelancers_server.exceptions.JobNotFoundException;
import com.vitrine_freelancers_server.exceptions.UserNotAuthorizationException;
import com.vitrine_freelancers_server.repositories.ApplicationRepository;
import com.vitrine_freelancers_server.repositories.JobRepository;
import com.vitrine_freelancers_server.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    private UserPrincipalDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UserPrincipalDTO userPrincipal)) {
            throw new UserNotAuthorizationException("Usuário não autenticado");
        }
        return userPrincipal;
    }

    @Transactional
    public ApplicationEntity apply(Long jobId) {
        UserPrincipalDTO principal = getCurrentUser();

        JobEntity job = jobRepository.findById(jobId)
                .filter(j -> Boolean.TRUE.equals(j.getOpen()))
                .orElseThrow(() -> new JobNotFoundException("Vaga não encontrada ou já encerrada"));

        if (applicationRepository.existsByJobIdAndUserId(jobId, principal.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Você já se candidatou a esta vaga");
        }

        UserEntity userRef = userRepository.getReferenceById(principal.getId());

        ApplicationEntity application = ApplicationEntity.builder()
                .job(job)
                .user(userRef)
                .status(ApplicationStatus.PENDING)
                .build();

        job.setApplicationsCount(job.getApplicationsCount() != null ? job.getApplicationsCount() + 1 : 1);
        job.setHasNewApplications(true);
        jobRepository.save(job);

        return applicationRepository.save(application);
    }

    public List<ApplicationEntity> getMyApplications() {
        UserPrincipalDTO principal = getCurrentUser();
        return applicationRepository.findByUserIdOrderByCreatedAtDesc(principal.getId());
    }

    @Transactional
    public List<ApplicationEntity> getJobApplications(Long jobId) {
        UserPrincipalDTO principal = getCurrentUser();

        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Vaga não encontrada"));

        boolean isAdmin = principal.getAuthorities() != null && principal.getAuthorities().contains("ROLE_ADMIN");
        if (!isAdmin && !job.getCompany().getId().equals(principal.getCompanyId())) {
            throw new UserNotAuthorizationException("Acesso negado");
        }

        job.setHasNewApplications(false);
        jobRepository.save(job);

        return applicationRepository.findByJobIdOrderByCreatedAtDesc(jobId);
    }

    @Transactional
    public ApplicationEntity updateStatus(Long applicationId, ApplicationStatus newStatus) {
        UserPrincipalDTO principal = getCurrentUser();

        ApplicationEntity application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidatura não encontrada"));

        boolean isAdmin = principal.getAuthorities() != null && principal.getAuthorities().contains("ROLE_ADMIN");
        if (!isAdmin && !application.getJob().getCompany().getId().equals(principal.getCompanyId())) {
            throw new UserNotAuthorizationException("Acesso negado");
        }

        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
