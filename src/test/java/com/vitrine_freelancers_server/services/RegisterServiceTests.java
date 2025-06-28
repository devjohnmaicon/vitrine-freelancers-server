package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.*;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.Permission;
import com.vitrine_freelancers_server.domain.Role;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.UserRole;
import com.vitrine_freelancers_server.enums.UserStatus;
import com.vitrine_freelancers_server.infra.security.TokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
        userDTO = new CreateUserDTO("user 1", "user1@email", "123", UserRole.COMPANY);
        companyDTO = new CreateCompanyDTO("company 1");
        permission = new Permission(1L, "PERMISSION_1");
        role = new Role(1L, "COMPNY", "Descrição", Set.of(user), Set.of(permission));
        user = new UserEntity(1L, "user 1", "user1@email", "123", UserStatus.ACTIVE, Set.of(role), company, LocalDateTime.now(), null);
        company = new CompanyEntity(1L, "company 1", user, List.of(), true, LocalDateTime.now(), LocalDateTime.now());
        token = "Bearer eyJhbGciOiJIUzI1NiJ9";
    }

    @Test
    public void testRegisterUserAndCompanySuccess() {
        RegisterUserCompanyDTO requestUserAndCompanyDTO = new RegisterUserCompanyDTO(userDTO, companyDTO);
        when(userService.createUser(userDTO)).thenReturn(user);
        when(companyService.createCompany(companyDTO, user)).thenReturn(company);
        when(tokenService.generateToken(user.getEmail())).thenReturn(token);

        ResponseTokenDTO responseToken = registerService.registerUserAndCompany(requestUserAndCompanyDTO);

        verify(userService).createUser(userDTO);
        verify(companyService).createCompany(companyDTO, user);
        verify(tokenService).generateToken(user.getEmail());

        Assertions.assertNotNull(token);
        Assertions.assertEquals(user.getEmail(), responseToken.email());
        Assertions.assertEquals(company.getName(), responseToken.company());
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
