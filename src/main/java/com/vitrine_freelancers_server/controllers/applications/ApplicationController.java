package com.vitrine_freelancers_server.controllers.applications;

import com.vitrine_freelancers_server.domain.ApplicationEntity;
import com.vitrine_freelancers_server.enums.ApplicationStatus;
import com.vitrine_freelancers_server.exceptions.response.ResponseSuccess;
import com.vitrine_freelancers_server.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private static final String STATUS_OK = "Success";

    @PostMapping
    public ResponseEntity<ResponseSuccess> apply(@RequestBody Map<String, Long> body) {
        Long jobId = body.get("jobId");
        ApplicationEntity application = applicationService.apply(jobId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseSuccess(STATUS_OK, HttpStatus.CREATED.value(), toResponse(application)));
    }

    @GetMapping("/my")
    public ResponseEntity<ResponseSuccess> getMyApplications() {
        List<ApplicationEntity> applications = applicationService.getMyApplications();
        return ResponseEntity.ok(new ResponseSuccess(STATUS_OK, HttpStatus.OK.value(),
                applications.stream().map(this::toResponse).toList()));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<ResponseSuccess> getJobApplications(@PathVariable Long jobId) {
        List<ApplicationEntity> applications = applicationService.getJobApplications(jobId);
        return ResponseEntity.ok(new ResponseSuccess(STATUS_OK, HttpStatus.OK.value(),
                applications.stream().map(this::toResponse).toList()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseSuccess> updateStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        ApplicationStatus newStatus = ApplicationStatus.valueOf(body.get("status"));
        ApplicationEntity updated = applicationService.updateStatus(id, newStatus);
        return ResponseEntity.ok(new ResponseSuccess(STATUS_OK, HttpStatus.OK.value(), toResponse(updated)));
    }

    private ApplicationResponse toResponse(ApplicationEntity app) {
        return new ApplicationResponse(
                app.getId(),
                app.getJob().getId(),
                app.getJob().getPosition(),
                app.getJob().getCompany().getName(),
                app.getUser().getId(),
                app.getUser().getName(),
                app.getStatus(),
                app.getCreatedAt(),
                app.getUpdatedAt()
        );
    }

    public record ApplicationResponse(
            Long id,
            Long jobId,
            String jobPosition,
            String companyName,
            Long userId,
            String userName,
            ApplicationStatus status,
            java.time.LocalDateTime createdAt,
            java.time.LocalDateTime updatedAt
    ) {}
}
