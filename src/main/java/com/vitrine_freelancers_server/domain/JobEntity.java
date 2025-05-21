package com.vitrine_freelancers_server.domain;

import com.vitrine_freelancers_server.enums.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "jobs")
public class JobEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;
    JobType type;
    String position;
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
    @UpdateTimestamp
    LocalDateTime updatedAt;

}
