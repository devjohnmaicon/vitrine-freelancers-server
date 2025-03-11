package com.vitrine_freelancers_server.controllers.jobs;

import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.mappers.JobMapper;
import com.vitrine_freelancers_server.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping
    public JobEntity createJob(@RequestBody JobRequests job) {
        return jobService.createJob(JobMapper.toEntity(job));
    }

    @GetMapping
    public List<JobEntity> getAllJobs() {
        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public JobEntity getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }


//    @PutMapping("/{id}")
//    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
//        return jobService.updateJob(id, job);
//    }

    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }
}
