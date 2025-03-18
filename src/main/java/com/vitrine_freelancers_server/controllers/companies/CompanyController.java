package com.vitrine_freelancers_server.controllers.companies;

import com.vitrine_freelancers_server.controllers.companies.requests.CompanyRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> createCompany(@RequestBody CompanyRequests requests) {
        try {
            return ResponseEntity.ok(companyService.createCompany(requests));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CompanyEntity>> getAllCompanies() {
        try {
            return ResponseEntity.ok(companyService.getAllCompanies());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyEntity> getCompanyById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(companyService.findCompanyById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyEntity> updateCompany(@PathVariable Long id, @RequestBody CompanyRequests request) {
        try {
            return ResponseEntity.ok(companyService.updateCompany(id, request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
