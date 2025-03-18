package com.vitrine_freelancers_server.controllers.jobs;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobRequests;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobRequests request) {
        try {
            return ResponseEntity.ok(jobService.createJob(request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getJobsOpen() {
        try {
            List<JobEntity> jobsOpen = jobService.findJobsOpen();
            return ResponseEntity.ok(JobMapper.toResponse(jobsOpen));
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
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobRequests requestUpdate) {
        try {
            JobEntity jobUpdated = jobService.updateJob(id, requestUpdate);
            return ResponseEntity.ok(JobMapper.toResponse(jobUpdated));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public void closeJob(@PathVariable Long id) {
        try {
            jobService.closeJob(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<?> getJobsByCompany(@PathVariable Long id) {
        try {
            List<JobEntity> jobsByCompany = jobService.findJobsByCompany(id);
            return ResponseEntity.ok(JobMapper.toResponse(jobsByCompany));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
