package com.vitrine_freelancers_server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@Entity(name = "companies")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id = null;

    @Column(unique = true)
    String name;


    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    UserEntity user;

    @JsonIgnore
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<JobEntity> jobs = new ArrayList<>();

    Boolean isActive = true;

    @CreationTimestamp
    LocalDateTime createdAt = LocalDateTime.now();
    @UpdateTimestamp
    LocalDateTime updatedAt;

    public CompanyEntity(Long id, String name, UserEntity user, List<JobEntity> jobs, boolean isOpen, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.jobs = jobs;
        this.isActive = isOpen;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
