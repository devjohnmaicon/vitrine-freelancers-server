package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.dtos.job.JobProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {
    Page<JobEntity> findJobEntitiesByOpenIsTrue(Pageable pageable);

    List<JobEntity> findByCompanyId(Long id);

    Optional<JobEntity> findJobEntityByIdAndOpenIsTrue(Long id);

    List<JobEntity> findJobEntityByType(Long typeId);

    List<JobProjection> findJobsByOpenIsTrueAndOpenUntilEquals(LocalDateTime openUntil);

    @Modifying
    @Transactional
    @Query(value = "UPDATE jobs SET open = false WHERE id IN (:ids)", nativeQuery = true)
    int closeJobsByIds(@Param("ids") List<Long> ids);
}
