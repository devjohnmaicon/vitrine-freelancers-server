package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.companies.requests.CompanyRequests;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.mappers.CompanyMapper;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    public CompanyEntity createCompany(CompanyRequests companyRequest) {
        UserEntity user = userService.findUserById(companyRequest.user_id());
        if (companyRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("O usuário já possui uma empresa cadastrada");
        }
        return companyRepository.save(CompanyMapper.toEntity(companyRequest, user));
    }

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public CompanyEntity findCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Company not found"));
    }

    public CompanyEntity updateCompany(Long id, CompanyRequests companyRequests) {
        CompanyEntity company = findCompanyById(id);
        company.setName(companyRequests.name());
        return companyRepository.save(company);
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
