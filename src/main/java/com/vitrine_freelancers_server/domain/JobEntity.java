package com.vitrine_freelancers_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    Boolean open = true;
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    CompanyEntity company;

    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = LocalDateTime.now();

}
