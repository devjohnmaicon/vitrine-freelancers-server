package com.vitrine_freelancers_server.controllers.companies;

import com.vitrine_freelancers_server.controllers.companies.requests.CompanyRequests;
import com.vitrine_freelancers_server.controllers.companies.response.CompanyResponse;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.mappers.CompanyMapper;
import com.vitrine_freelancers_server.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        try {
            List<CompanyEntity> allCompanies = companyService.getAllCompanies();
            return ResponseEntity.ok(CompanyMapper.toResponse(allCompanies));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable Long id) {
        try {
            CompanyEntity company = companyService.findCompanyById(id);
            return ResponseEntity.ok(CompanyMapper.toResponse(company));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @RequestBody CompanyRequests request) {
        try {
            CompanyEntity companyupdated = companyService.updateCompany(id, request);
            return ResponseEntity.ok(CompanyMapper.toResponse(companyupdated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableCompany(@PathVariable Long id) {
        try {
            companyService.disableCompany(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
