package com.vitrine_freelancers_server.repositories;

import com.vitrine_freelancers_server.domain.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Long> {

}
