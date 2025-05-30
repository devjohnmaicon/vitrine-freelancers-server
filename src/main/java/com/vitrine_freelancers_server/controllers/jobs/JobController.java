package com.vitrine_freelancers_server.controllers.jobs;

import com.vitrine_freelancers_server.controllers.jobs.requests.JobUpdateRequest;
import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.response.ResponseSuccess;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.services.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {
    private final JobService jobService;

    private JobController(JobService jobService) {
        this.jobService = jobService;
    }

    private final String DEFAULT_STATUS = "success";

    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody JobUpdateRequest request) {
        JobEntity createdJob = jobService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseSuccess(DEFAULT_STATUS, HttpStatus.CREATED.value(), JobMapper.toResponse(createdJob)));
    }

    @GetMapping
    public ResponseEntity<?> getJobsOpen(@RequestParam(required = false, defaultValue = "0") int page,
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
        JobEntity job = jobService.findJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess(DEFAULT_STATUS, HttpStatus.OK.value(), JobMapper.toResponse(job)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ResponseSuccess> updateJob(@PathVariable Long id, @RequestBody JobUpdateRequest jobUpdate,
                                                     @AuthenticationPrincipal UserEntity userPrincipal
    ) {

        JobEntity jobUpdated = jobService.updateJob(id, jobUpdate, userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess(
                DEFAULT_STATUS, HttpStatus.OK.value(), JobMapper.toResponse(jobUpdated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSuccess> closeJob(@PathVariable Long id, @AuthenticationPrincipal UserEntity userPrincipal) {
        jobService.closeJob(id, userPrincipal);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ResponseSuccess(DEFAULT_STATUS, HttpStatus.ACCEPTED.value(), null));
    }

    @GetMapping("/company")
    public ResponseEntity<?> getJobsByCompany(@AuthenticationPrincipal UserEntity userPrincipal) {
        List<JobEntity> jobsByCompany = jobService.findJobsByCompany(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseSuccess(DEFAULT_STATUS, HttpStatus.OK.value(), JobMapper.toResponse(jobsByCompany)));

    }

}
