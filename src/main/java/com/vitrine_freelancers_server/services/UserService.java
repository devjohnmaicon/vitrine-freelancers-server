package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserEntity createUser(UserEntity user) {
        findByEmail(user.getEmail());
        return userRepository.save(user);
    }

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Id not found"));
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Email not found"));
    }

    public UserEntity updateUser(Long id, UserEntity user) {
        return null;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
