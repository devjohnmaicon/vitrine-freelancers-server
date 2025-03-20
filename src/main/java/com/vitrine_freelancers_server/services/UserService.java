package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.LoginRequestDTO;
import com.vitrine_freelancers_server.controllers.authentication.RegisterRequestDTO;
import com.vitrine_freelancers_server.controllers.authentication.ResponseTokenDTO;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.infra.security.TokenService;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenService tokenService;

    public ResponseTokenDTO createUser(RegisterRequestDTO registerRequestDTO) {
        findByEmail(registerRequestDTO.email()).ifPresent(user -> {
            throw new UsernameNotFoundException("User already exists");
        });
        UserEntity user = UserEntity.builder()
                .email(registerRequestDTO.email())
                .name(registerRequestDTO.name())
                .password(passwordEncoder.encode(registerRequestDTO.password()))
                .role(registerRequestDTO.role())
                .build();
        ;
        userRepository.save(user);

        String token = tokenService.generateToken(user.getEmail());
        return new ResponseTokenDTO(user.getEmail(), token);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity updateUser(Long id, UserEntity user) {
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public ResponseTokenDTO login(LoginRequestDTO requestDTO) {
        UserEntity user = userRepository.findByEmail(requestDTO.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail());
            return new ResponseTokenDTO(user.getEmail(), token);
        } else {
            throw new RuntimeException("Invalid user or password");
        }
    }
}
