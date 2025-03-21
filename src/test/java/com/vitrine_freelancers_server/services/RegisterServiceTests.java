package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.controllers.authentication.CreateUserDTO;
import com.vitrine_freelancers_server.controllers.authentication.RegisterService;
import com.vitrine_freelancers_server.controllers.authentication.RegisterUserCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.UserRole;
import com.vitrine_freelancers_server.infra.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor
public class RegisterServiceTests {

    @Autowired
    private RegisterService registerService;
    private UserService userService;

    private UserEntity user;
    private CompanyEntity company;
    private CreateUserDTO userDTO;
    private CreateCompanyDTO companyDTO;
    private CompanyService companyService;
    private TokenService tokenService;
    private String token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new CreateUserDTO("teste - user 1", "user1@email", "123", UserRole.USER);
        companyDTO = new CreateCompanyDTO("teste - company 1");
        user = new UserEntity(1L, "teste - user 1", "user1@email", "123", UserRole.USER, LocalDateTime.now(), null);
        company = new CompanyEntity(1L, "teste - company 1", user, List.of(), true, LocalDateTime.now(), LocalDateTime.now());
        token = "Bearer eyJhbGciOiJIUzI1NiJ9";
    }

    @Test
    public void testRegisterUserAndCompanySuccess() {
        RegisterUserCompanyDTO request = new RegisterUserCompanyDTO(userDTO, companyDTO);

        when(userService.createUser(request.user())).thenReturn(user);
        when(companyService.createCompany(companyDTO, user.getId())).thenReturn(company);
        when(tokenService.generateToken(user.getEmail())).thenReturn(token);
    }

    @Test
    public void testRegisterUserAndCompnyFailure() {
        RegisterUserCompanyDTO request = new RegisterUserCompanyDTO(userDTO, companyDTO);

        when(userService.createUser(request.user())).thenThrow(new RuntimeException("Error"));
        when(companyService.createCompany(companyDTO, user.getId())).thenThrow(new RuntimeException("Error"));
    }
}
