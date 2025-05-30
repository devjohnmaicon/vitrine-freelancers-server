package com.vitrine_freelancers_server.controllers.companies;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.response.ResponseSuccess;
import com.vitrine_freelancers_server.mappers.CompanyMapper;
import com.vitrine_freelancers_server.services.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final String DEFAULT_STATUS = "success";
    private final CompanyService companyService;

    CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<?> companies() {
        List<CompanyEntity> allCompanies = companyService.getAllCompanies();
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseSuccess(
                        DEFAULT_STATUS,
                        HttpStatus.OK.value(),
                        allCompanies.stream().map(CompanyMapper::toResponse).toList()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> companyById(@PathVariable Long id) {
        CompanyEntity company = companyService.companyById(id);
        return ResponseEntity.ok(new ResponseSuccess(
                DEFAULT_STATUS,
                HttpStatus.OK.value(),
                CompanyMapper.toResponse(company)
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseSuccess> updateCompany(@PathVariable Long id, @RequestBody CreateCompanyDTO request,
                                                         @AuthenticationPrincipal UserEntity user
    ) {
        CompanyEntity companyupdated = companyService.updateCompany(id, request, user);
        return ResponseEntity.ok().body(
                new ResponseSuccess(
                        DEFAULT_STATUS,
                        HttpStatus.OK.value(),
                        CompanyMapper.toResponse(companyupdated)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSuccess> disableCompany(@PathVariable Long id) {
        companyService.inactivatedCompany(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ResponseSuccess(
                        DEFAULT_STATUS,
                        HttpStatus.ACCEPTED.value(),
                        null
                )
        );
    }
}
