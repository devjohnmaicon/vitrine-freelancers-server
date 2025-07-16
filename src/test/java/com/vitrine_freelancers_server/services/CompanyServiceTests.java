package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.auth.dtos.CreateCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.CompanyAlreadyExistsException;
import com.vitrine_freelancers_server.exceptions.CompanyNotFoundException;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import com.vitrine_freelancers_server.utils.MockDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompanyServiceTests {

    @Mock
    private CompanyRepository companyRepository;

    @Spy
    @InjectMocks
    private CompanyService companyService;

    private CompanyEntity company;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = MockDataFactory.createUser();
        company = MockDataFactory.createCompany(
                user,
                List.of(
                        MockDataFactory.createJobEntity("deliveryman"),
                        MockDataFactory.createJobEntity("sushiman"),
                        MockDataFactory.createJobEntity("sushiman")
                )
        );
    }


    @Test
    void createCompanySuccessfully() {
        CreateCompanyDTO companyDTO = new CreateCompanyDTO("Farmácia do João");

        when(companyRepository.existsByName(companyDTO.name())).thenReturn(false);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(company);

        CompanyEntity result = companyService.createCompany(companyDTO, user);

        verify(companyRepository).existsByName(companyDTO.name());
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));

        assertNotNull(result);
        assertEquals(companyDTO.name(), result.getName());
        assertEquals(user.getId(), result.getId());
    }

    @Test
    void SholdThrowExceptionWhenCompanyExists() {
        CreateCompanyDTO companyDTO = new CreateCompanyDTO("Company 1");
        when(companyRepository.existsByName(companyDTO.name())).thenReturn(true);

        Exception exception = assertThrows(CompanyAlreadyExistsException.class, () -> companyService.createCompany(companyDTO, user));

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

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(company);

        CompanyEntity result = companyService.updateCompany(1L, requestUpdate, user);

        assertNotNull(result);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void inactivatedCompanySuccessfully() {
        Long companyId = 1L;
        CompanyEntity companyFound = company;
        CompanyEntity companyUpdated = companyFound.toBuilder()
                .name(companyFound.getName() + " - Desativada")
                .isActive(false)
                .build();

        when(companyRepository.findById(companyId)).thenReturn(Optional.of(companyFound));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyUpdated);

        companyService.inactivateCompany(companyId);

        verify(companyRepository, times(1)).findById(1L);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }
}