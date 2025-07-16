package com.vitrine_freelancers_server.services;


import com.vitrine_freelancers_server.controllers.jobs.requests.JobCreateOrUpdateRequest;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.Permission;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.dtos.User.UserPrincipalDTO;
import com.vitrine_freelancers_server.enums.JobType;
import com.vitrine_freelancers_server.exceptions.JobNotFoundException;
import com.vitrine_freelancers_server.repositories.JobRepository;
import com.vitrine_freelancers_server.utils.MockDataFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTests {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyService companyService;

    @Spy
    @InjectMocks
    private JobService jobService;

    private Permission permission;
    private CompanyEntity company;
    private UserEntity user;
    private JobEntity job;
    private UserPrincipalDTO userPrincipal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
        doReturn(userPrincipal).when(jobService).getUser();
    }

    private void setupTestData() {
        permission = MockDataFactory.createPermission();
        company = MockDataFactory.createCompany(null, null);
        user = MockDataFactory.createUser();
        job = MockDataFactory.createJobEntity(null);
        userPrincipal = MockDataFactory.createUserPrincipal();
    }

    @Test
    void createJobSuccessfully() {
        JobCreateOrUpdateRequest request = new JobCreateOrUpdateRequest(
                JobType.FREELANCER,
                "deliveryman",
                "Vaga para motoentregador",
                "2021-10-10",
                "14:00",
                "18:00",
                100.0,
                "Possuir moto própria"
        );
        JobEntity jobEntity = new JobEntity(
                1L,
                JobType.FREELANCER,
                "deliveryman",
                "Vaga para motoentregador",
                "2021-10-10",
                "14:00",
                "18:00",
                100.0,
                "Possuir moto própria",
                true,
                this.company,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyService.companyById(anyLong())).thenReturn(company);
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        JobEntity result = jobService.createJob(request);

        assertNotNull(result);
        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }

    @Test
    void findJobsOpenSuccessfully() {
        when(jobRepository.findJobEntitiesByOpenIsTrue(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(job)));
        JobEntity result = jobService.findAllJobsOpen(PageRequest.of(0, 10)).getContent().getFirst();
        assertNotNull(result);
        assertEquals(job, result);
        verify(jobRepository, times(1)).findJobEntitiesByOpenIsTrue(any());
    }

    @Test
    void findJobByIdSuccessfully() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        JobEntity result = jobService.findJobById(1L);

        assertNotNull(result);
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should throw JobNotFoundException when job ID does not exist")
    void findJobByIdNotFound() {
        Long nonExistentId = 2L;
        when(jobRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> jobService.findJobById(nonExistentId))
                .isInstanceOf(JobNotFoundException.class)
                .hasMessage("Job " + nonExistentId + " not found");

        verify(jobRepository).findById(nonExistentId);
    }

    @Test
    void updateJobSuccessfully() {
        JobCreateOrUpdateRequest requestUpdate = new JobCreateOrUpdateRequest(
                JobType.FREELANCER,
                "deliveryman",
                "Vaga para motorista",
                "2021-10-10",
                "14:00",
                "18:00",
                150.0,
                "Possuir moto própria"
        );

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.findJobEntityByIdAndOpenIsTrue(1L)).thenReturn(Optional.of(job));
        when(companyService.findCompanyByUser(user)).thenReturn(company);
        when(jobRepository.save(any(JobEntity.class))).thenReturn(job);

        JobEntity result = jobService.updateJob(1L, requestUpdate, user);
        assertNotNull(result);
        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }

    @Test
    void closeJobSuccessfully() {
        JobEntity jobClosed = new JobEntity(
                1L,
                JobType.FREELANCER,
                "deliveryman",
                "Vaga para motorista",
                "2021-10-10",
                "14:00",
                "18:00",
                150.0,
                "Possuir moto própria",
                false,
                company,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobClosed);
        when(jobRepository.findJobEntityByIdAndOpenIsTrue(1L)).thenReturn(Optional.of(job));
        when(companyService.findCompanyByUser(user)).thenReturn(company);

        jobService.closeJob(1L);

        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }
}
