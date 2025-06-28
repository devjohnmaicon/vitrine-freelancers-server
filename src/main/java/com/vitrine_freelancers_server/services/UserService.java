package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateUserDTO;
import com.vitrine_freelancers_server.controllers.authentication.ResponseLoginDTO;
import com.vitrine_freelancers_server.controllers.authentication.requests.LoginRequest;
import com.vitrine_freelancers_server.controllers.users.UserUpdateRequest;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.enums.UserStatus;
import com.vitrine_freelancers_server.exceptions.InvalidLoginException;
import com.vitrine_freelancers_server.exceptions.UserEmailAlreadyExistsException;
import com.vitrine_freelancers_server.exceptions.UserNotFoundException;
import com.vitrine_freelancers_server.infra.security.TokenService;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;
    final TokenService tokenService;
    final CompanyService companyService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, CompanyService companyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.companyService = companyService;
    }

    @Transactional
    public UserEntity createUser(CreateUserDTO userDTO) {
        checkIfUserExists(userDTO.email());

        UserEntity user = UserEntity.builder().email(userDTO.email()).name(userDTO.name()).password(passwordEncoder.encode(userDTO.password())).status(UserStatus.ACTIVE).build();
        ;

        return userRepository.save(user);
    }

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void checkIfUserExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailAlreadyExistsException();
        }
    }

    public UserEntity updateUser(Long id, UserUpdateRequest user) {
        return null;
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        user.setStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }

    public ResponseLoginDTO login(LoginRequest requestDTO) {
        UserEntity user = userRepository.findByEmail(requestDTO.email()).orElseThrow(InvalidLoginException::new);
        CompanyEntity company = companyService.findCompanyByUser(user);
        if (passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail());
            return new ResponseLoginDTO(user.getEmail(), company.getId(), token);
        } else {
            throw new InvalidLoginException();
        }
    }

    public static Authentication getUser() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
    }
}
