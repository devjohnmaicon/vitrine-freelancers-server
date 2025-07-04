package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.JobEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
    Page<JobEntity> findJobEntitiesByOpenIsTrue(Pageable pageable);

    List<JobEntity> findByCompanyId(Long id);

    Optional<JobEntity> findJobEntityByIdAndOpenIsTrue(Long id);

    List<JobEntity> findJobEntityByType(Long typeId);
}
