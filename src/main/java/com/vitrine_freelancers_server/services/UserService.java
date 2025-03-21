package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateUserDTO;
import com.vitrine_freelancers_server.controllers.authentication.ResponseLoginDTO;
import com.vitrine_freelancers_server.controllers.authentication.requests.LoginRequest;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.infra.security.TokenService;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public UserEntity createUser(CreateUserDTO userDTO) {
        findByEmail(userDTO.email()).ifPresent(user -> {
            throw new UsernameNotFoundException("E-mail " + user.getEmail() + " already exists");
        });
        UserEntity user = UserEntity.builder().email(userDTO.email()).name(userDTO.name()).password(passwordEncoder.encode(userDTO.password())).role(userDTO.role()).build();
        ;
        return userRepository.save(user);
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

    public ResponseLoginDTO login(LoginRequest requestDTO) {
        UserEntity user = userRepository.findByEmail(requestDTO.email()).orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(requestDTO.password(), user.getPassword())) {
            String token = tokenService.generateToken(user.getEmail());
            return new ResponseLoginDTO(user.getEmail(), token);
        } else {
            throw new RuntimeException("Invalid user or password");
        }
    }
}
