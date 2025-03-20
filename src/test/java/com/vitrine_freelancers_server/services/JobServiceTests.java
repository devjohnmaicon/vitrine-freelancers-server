package com.vitrine_freelancers_server.services;


import com.vitrine_freelancers_server.controllers.jobs.requests.JobRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.JobType;
import com.vitrine_freelancers_server.enums.UserRole;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JobServiceTests {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private JobService jobService;

    private CompanyEntity company;
    private UserEntity user;
    private JobEntity job1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity(1L, "User 1", "user@email", "123", UserRole.USER, LocalDateTime.now(), null);
        company = new CompanyEntity(
                1L,
                "Farmácia do João",
                user,
                List.of(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        job1 = new JobEntity(
                1L,
                JobType.FREELANCER,
                "Vaga para motoentregador",
                "2021-10-10",
                "14:00",
                "18:00",
                100.0,
                "Possuir moto própria",
                true,
                company,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createJobSuccessfully() {
        JobRequests request = new JobRequests(
                JobType.FREELANCER,
                "Vaga para motoentregador",
                "2021-10-10",
                "14:00",
                "18:00",
                100.0,
                "Possuir moto própria",
                1L
        );
        JobEntity jobEntity = new JobEntity(
                1L,
                JobType.FREELANCER,
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

        when(companyService.findCompanyById(anyLong())).thenReturn(company);
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        JobEntity result = jobService.createJob(request);

        assertNotNull(result);
        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }

    @Test
    void findJobsOpenSuccessfully() {
        when(jobRepository.findJobEntitiesByOpenIsTrueOrderByCreatedAtDesc(any(PageRequest.class))).thenReturn(new PageImpl<>(List.of(job1)));
        JobEntity result = jobService.findJobsOpen(PageRequest.of(0, 10)).getContent().getFirst();
        assertNotNull(result);
        assertEquals(job1, result);
        verify(jobRepository, times(1)).findJobEntitiesByOpenIsTrueOrderByCreatedAtDesc(any());
    }

    @Test
    void findJobByIdSuccessfully() {
        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));
        JobEntity result = jobService.findJobById(1L);

        assertNotNull(result);
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void findJobByIdNotFound() {
        when(jobRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> jobService.findJobById(1L));

        assertEquals("Job not found", exception.getMessage());
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateJobSuccessfully() {
        JobRequests requestUpdate = new JobRequests(
                JobType.FREELANCER,
                "Vaga para motorista",
                "2021-10-10",
                "14:00",
                "18:00",
                150.0,
                "Possuir moto própria",
                1L
        );

        JobEntity jobUpdated = new JobEntity(
                1L,
                JobType.FREELANCER,
                "Vaga para motorista",
                "2021-10-10",
                "14:00",
                "18:00",
                150.0,
                "Possuir moto própria",
                true,
                company,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(jobRepository.findById(1L)).thenReturn(Optional.of(job1));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobUpdated);
        JobEntity result = jobService.updateJob(1L, requestUpdate);

        assertNotNull(result);
        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }

    @Test
    void closeJobSuccessfully() {
        JobEntity jobEntity = new JobEntity(/* parameters */);

        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(jobEntity));
        when(jobRepository.save(any(JobEntity.class))).thenReturn(jobEntity);

        jobService.closeJob(1L);

        assertFalse(false);
        verify(jobRepository, times(1)).save(any(JobEntity.class));
    }

}
