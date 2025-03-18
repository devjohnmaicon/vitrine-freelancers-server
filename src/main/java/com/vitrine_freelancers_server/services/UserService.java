package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        findByEmail(user.getEmail()).ifPresent(u -> {
            throw new RuntimeException("Email already exists");
        });
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
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
}
