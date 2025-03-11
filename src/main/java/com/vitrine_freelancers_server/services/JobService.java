package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.domain.JobEntity;
import com.vitrine_freelancers_server.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public List<JobEntity> getAllJobs() {
        return jobRepository.findAll();
    }

    public JobEntity getJobById(Long id) {
        return jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found"));
    }

    public JobEntity createJob(JobEntity jobEntity) {
        return jobRepository.save(jobEntity);
    }

//    public Job updateJob(Long id, Job updatedJob) {
//        for (int i = 0; i < jobs.size(); i++) {
//            if (jobs.get(i).getId().equals(id)) {
//                jobs.set(i, updatedJob);
//                return updatedJob;
//            }
//        }
//        return null;
//    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}