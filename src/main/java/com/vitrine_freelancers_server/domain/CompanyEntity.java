package com.vitrine_freelancers_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@Entity(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;
    String name;

    @OneToOne
    UserEntity user;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<JobEntity> jobs;

    @CreationTimestamp
    LocalDateTime created_at = LocalDateTime.now();
    LocalDateTime updated_at = LocalDateTime.now();

    public CompanyEntity(Long id, String name, UserEntity user, List<JobEntity> jobs, LocalDateTime created_at, LocalDateTime updated_at) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.jobs = jobs;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
}
