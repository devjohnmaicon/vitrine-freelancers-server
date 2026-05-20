package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long> {
    List<ApplicationEntity> findByJobIdOrderByCreatedAtDesc(Long jobId);
    List<ApplicationEntity> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByJobIdAndUserId(Long jobId, Long userId);
}
