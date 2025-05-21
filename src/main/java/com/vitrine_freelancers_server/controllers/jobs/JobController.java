package com.vitrine_freelancers_server.controllers.jobs;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.services.CompanyService;
import com.vitrine_freelancers_server.services.JobService;
import com.vitrine_freelancers_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobRequests request) {
        try {
            JobEntity createdJob = jobService.createJob(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(JobMapper.toResponse(createdJob));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getJobsOpen(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "DESC") String sort
    ) {
        try {
            Sort.Direction direction = sort.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "createdAt"));
            Page<JobEntity> jobsOpen = jobService.findJobsOpen(pageable);
            return ResponseEntity.ok(JobMapper.toResponse(jobsOpen.getContent()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
        try {
            JobEntity job = jobService.findJobById(id);
            return ResponseEntity.ok(JobMapper.toResponse(job));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobRequests requestUpdate) {
        try {
            JobEntity jobUpdated = jobService.updateJob(id, requestUpdate);
            return ResponseEntity.ok(JobMapper.toResponse(jobUpdated));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity closeJob(@PathVariable Long id) {
        try {
            jobService.closeJob(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/company")
    @PreAuthorize("#id == principal.id")
    public ResponseEntity<?> getJobsByCompany() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUserEmail = authentication.getName();
            UserEntity user = userService.findByEmail(currentUserEmail).orElseThrow(() -> new RuntimeException("Erro ao buscar jobs do usuário"));
            CompanyEntity company = companyService.findCompanyByUser(user);
            List<JobEntity> jobsByCompany = jobService.findJobsByCompany(company.getId());
            return ResponseEntity.ok(JobMapper.toResponse(jobsByCompany));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
