package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateUserDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.Permission;
import com.vitrine_freelancers_server.domain.Role;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.UserRole;
import com.vitrine_freelancers_server.enums.UserStatus;
import com.vitrine_freelancers_server.exceptions.UserEmailAlreadyExistsException;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserEntity user;
    private CompanyEntity company;
    private Role role;
    private Permission permission;

    @BeforeEach
    void setUp() {
        permission = new Permission(1L, "PERMISSION_1");
        role = new Role(1L, "COMPNY", "Descrição", Set.of(user), Set.of(permission));
        user = new UserEntity(1L, "user 1", "user1@email", "12345", UserStatus.ACTIVE, Set.of(role), company, LocalDateTime.now(), null);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserDTO userDTO = new CreateUserDTO("user1@email.com", "123456", "User 1", UserRole.COMPANY);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity createdUser = userService.createUser(userDTO);

        verify(userRepository).existsByEmail(userDTO.email());
        verify(userRepository).save(any(UserEntity.class));

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals(userDTO.email(), createdUser.getEmail());
        Assertions.assertEquals(userDTO.name(), createdUser.getName());
        Assertions.assertEquals(UserStatus.ACTIVE, createdUser.getStatus());
    }

    @Test
    void testCreateUserWithExistingEmail() {
        CreateUserDTO userDTO = new CreateUserDTO("user1@email.com", "123456", "User 1", UserRole.COMPANY);

        when(userRepository.existsByEmail(userDTO.email())).thenReturn(true);
        RuntimeException exception = Assertions.assertThrows(UserEmailAlreadyExistsException.class, () -> {
            userService.createUser(userDTO);
        });

        Assertions.assertEquals("The email already exists.", exception.getMessage());
    }
}
