package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.JobType;
import com.vitrine_freelancers_server.enums.UserStatus;
import com.vitrine_freelancers_server.exceptions.CompanyAlreadyExistsException;
import com.vitrine_freelancers_server.exceptions.CompanyNotFoundException;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyService companyService;

    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        company = new CompanyEntity(
                1L,
                "Company 1",
                createUser(),
                List.of(
                        new JobEntity(1L, JobType.FREELANCER, "deliveryman", "Vaga para motoentregador", "2021-10-10", "14:00", "18:00", 100.0, "Possuir moto própria", true, null, LocalDateTime.now(), LocalDateTime.now()),
                        new JobEntity(2L, JobType.FREELANCER, "sushiman", "Vaga para garçon", "2021-10-10", "14:00", "18:00", 180.0, "Possuir moto própria", true, null, LocalDateTime.now(), LocalDateTime.now()),
                        new JobEntity(3L, JobType.FIXO, "sushiman", "Vaga para cozinheiro", "2021-10-10", "14:00", "18:00", 110.0, "Possuir moto própria", true, null, LocalDateTime.now(), LocalDateTime.now())
                ),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createCompanySuccessfully() {
        CreateCompanyDTO companyDTO = new CreateCompanyDTO("Company 1");
        UserEntity user = createUser();

        when(companyRepository.existsByName(companyDTO.name())).thenReturn(false);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(company);

        CompanyEntity result = companyService.createCompany(companyDTO, user);

        verify(companyRepository).existsByName(companyDTO.name());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));

        assertNotNull(result);
        assertEquals(companyDTO.name(), result.getName());
        assertEquals(user.getId(), result.getUser().getId());
    }

    @Test
    void SholdThrowExceptionWhenCompanyExists() {
        CreateCompanyDTO companyDTO = new CreateCompanyDTO("Company 1");
        when(companyRepository.existsByName(companyDTO.name())).thenReturn(true);

        Exception exception = assertThrows(CompanyAlreadyExistsException.class, () -> companyService.createCompany(companyDTO, createUser()));

        verify(companyRepository).existsByName(companyDTO.name());
        verify(companyRepository, never()).findById(anyLong());

        assertEquals("There is already a company with this name.", exception.getMessage());
    }

    @Test
    void SholdBeFoundCompanyById() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        CompanyEntity result = companyService.companyById(1L);

        assertNotNull(result);
        assertEquals(result.getJobs(), company.getJobs());
        verify(companyRepository, times(1)).findById(anyLong());
    }


    @Test
    void shouldThrowExceptionWhenCompanyNotFoundById() {
        Long companyid = 1L;
        when(companyRepository.findById(companyid)).thenReturn(Optional.empty());
        Exception exception = assertThrows(CompanyNotFoundException.class, () -> companyService.companyById(companyid));

        verify(companyRepository, times(1)).findById(anyLong());
        assertEquals("ID: " + company.getId() + " - Empresa não encontrada", exception.getMessage());
    }

    @Test
    void updateCompanySuccessfully() {
        CreateCompanyDTO requestUpdate = new CreateCompanyDTO("Farmácia do João Updated");
        UserEntity user = createUser();
        CompanyEntity companyUpdated = new CompanyEntity(
                1L,
                "Farmácia do João Updated",
                user,
                List.of(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyUpdated);
        CompanyEntity result = companyService.updateCompany(1L, requestUpdate, user);

        assertNotNull(result);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void inactivatedCompanySuccessfully() {
        Long companyId = 1L;
        CompanyEntity companyFound = createCompany();
        CompanyEntity companyUpdated = companyFound.toBuilder()
                .name(companyFound.getName() + " - Desativada")
                .isActive(false)
                .build();
        ;

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyFound));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyUpdated);

        companyService.inactivateCompany(companyId);

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    private UserEntity createUser() {
        return UserEntity.builder()
                .id(1L)
                .name("User 1")
                .email("")
                .password("123456")
                .status(UserStatus.ACTIVE)
                .updated_at(null)
                .created_at(LocalDateTime.now())
                .build();
    }

    private CompanyEntity createCompany() {
        return CompanyEntity.builder()
                .id(1L)
                .name("Farmácia do João")
                .user(createUser())
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}