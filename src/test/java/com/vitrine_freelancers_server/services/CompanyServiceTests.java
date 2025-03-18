package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.companies.requests.CompanyRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CompanyServiceTests {
    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CompanyService companyService;

    private UserEntity user;
    private CompanyEntity company;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new UserEntity(1L, "User 1", "user@email", LocalDateTime.now(), null);
        company = new CompanyEntity(
                1L,
                "Farmácia do João",
                user,
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    @Test
    void createCompanySuccessfully() {
        CompanyRequests request = new CompanyRequests("Farmácia do João", 1L);
        when(userService.findUserById(1L)).thenReturn(user);
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(company);

        CompanyEntity result = companyService.createCompany(request);

        assertNotNull(result);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void findCompanyByIdSuccessfully() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        CompanyEntity result = companyService.findCompanyById(1L);

        assertNotNull(result);
        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    void findCompanyByIdNotFound() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> companyService.findCompanyById(1L));

        assertEquals("Company not found", exception.getMessage());
        verify(companyRepository, times(1)).findById(anyLong());
    }

    @Test
    void updateCompanySuccessfully() {
        CompanyRequests requestUpdate = new CompanyRequests("Farmácia do João Updated", 1L);
        CompanyEntity companyUpdated = new CompanyEntity(
                1L,
                "Farmácia do João Updated",
                user,
                List.of(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(any(CompanyEntity.class))).thenReturn(companyUpdated);
        CompanyEntity result = companyService.updateCompany(1L, requestUpdate);

        assertNotNull(result);
        verify(companyRepository, times(1)).save(any(CompanyEntity.class));
    }

    @Test
    void deleteCompanySuccessfully() {
        doNothing().when(companyRepository).deleteById(1L);
        companyService.deleteCompany(1L);
        verify(companyRepository, times(1)).deleteById(anyLong());
    }
}