package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getUserEntityByEmail(String email);

    Optional<UserEntity> findByEmail(String email);


}
