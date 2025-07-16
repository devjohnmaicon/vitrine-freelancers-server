package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.auth.RegisterService;
import com.vitrine_freelancers_server.controllers.auth.dtos.CreateCompanyDTO;
import com.vitrine_freelancers_server.controllers.auth.dtos.CreateUserDTO;
import com.vitrine_freelancers_server.controllers.auth.requests.RegisterUserCompanyDTO;
import com.vitrine_freelancers_server.controllers.auth.response.ResponseToken;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.Permission;
import com.vitrine_freelancers_server.domain.Role;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.UserRole;
import com.vitrine_freelancers_server.infra.security.TokenService;
import com.vitrine_freelancers_server.utils.MockDataFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterServiceTests {

    @Mock
    private UserService userService;
    @Mock
    private TokenService tokenService;
    @Mock
    private CompanyService companyService;

    @InjectMocks
    private RegisterService registerService;

    private UserEntity user;
    private CompanyEntity company;
    private Role role;
    private Permission permission;
    private CreateUserDTO userDTO;
    private CreateCompanyDTO companyDTO;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupTestData();
        token = "Bearer eyJhbGciOiJIUzI1NiJ9";
    }

    private void setupTestData() {
        permission = MockDataFactory.createPermission();
        company = MockDataFactory.createCompany(null, null);
        user = MockDataFactory.createUser();
    }

    @Test
    public void testRegisterUserAndCompanySuccess() {
        // Given
        userDTO = new CreateUserDTO("user@email", "password123", "John Doe", UserRole.COMPANY);
        companyDTO = new CreateCompanyDTO("Farmácia do João");
        RegisterUserCompanyDTO requestUserAndCompanyDTO = new RegisterUserCompanyDTO(userDTO, companyDTO);

        when(userService.createUser(userDTO)).thenReturn(user);
        when(companyService.createCompany(companyDTO, user)).thenReturn(company);
        when(tokenService.generateToken(anyString())).thenReturn(token);

        ResponseToken responseToken = registerService.registerUserAndCompany(requestUserAndCompanyDTO);

        verify(userService).createUser(userDTO);
        verify(companyService).createCompany(companyDTO, user);
        verify(tokenService).generateToken(user.getEmail());

        Assertions.assertNotNull(responseToken);
        Assertions.assertNotNull(responseToken.token());
        Assertions.assertEquals(userDTO.email(), responseToken.email());
        Assertions.assertEquals(companyDTO.name(), responseToken.company());
        Assertions.assertEquals(token, responseToken.token());
    }

    @Test
    public void sholdThrowExeptionWhenRegisterUserData() {
        RegisterUserCompanyDTO requestUserAndCompanyDTO = new RegisterUserCompanyDTO(userDTO, companyDTO);
        when(userService.createUser(userDTO)).thenThrow(new RuntimeException("User already exists"));
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            registerService.registerUserAndCompany(requestUserAndCompanyDTO);
        });

        verify(userService).createUser(userDTO);
        Assertions.assertEquals("User already exists", exception.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenRegisterCompanyData() {
        RegisterUserCompanyDTO requestUserAndCompanyDTO = new RegisterUserCompanyDTO(userDTO, companyDTO);
        when(userService.createUser(userDTO)).thenReturn(user);
        when(companyService.createCompany(companyDTO, user)).thenThrow(new RuntimeException("Company already exists"));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            registerService.registerUserAndCompany(requestUserAndCompanyDTO);
        });

        verify(userService).createUser(userDTO);
        verify(companyService).createCompany(companyDTO, user);
        Assertions.assertEquals("Company already exists", exception.getMessage());
    }

}
