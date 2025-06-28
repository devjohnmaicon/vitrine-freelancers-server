package com.vitrine_freelancers_server.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vitrine_freelancers_server.enums.JobType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

//@ToString(exclude = "company")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @ManyToOne(optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private CompanyEntity company;

    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @JsonBackReference
    public CompanyEntity getCompany() {
        return company;
    }

}
