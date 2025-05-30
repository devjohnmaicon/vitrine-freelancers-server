package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
    boolean findByUser_Id(Long userId);

    Optional<CompanyEntity> findByUserId(Long id);

    Optional<CompanyEntity> findByNameEquals(String name);

    Optional<CompanyEntity> findCompanyEntityByName(String name);

    boolean existsByName(String name);
}
