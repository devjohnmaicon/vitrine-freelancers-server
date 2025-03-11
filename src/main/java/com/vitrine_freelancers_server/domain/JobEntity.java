package com.vitrine_freelancers_server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "jobs")
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;
    String type;
    String description;
    String date;
    String startTime;
    String endTime;
    Double dailyValue;
    String requirements;
    Long companyId;
    @CreationTimestamp
    LocalDateTime created_at = LocalDateTime.now();
    LocalDateTime updated_at = LocalDateTime.now();

}
