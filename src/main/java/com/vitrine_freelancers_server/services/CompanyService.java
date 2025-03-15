package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public CompanyEntity createCompany(CompanyEntity company) {
        return companyRepository.save(company);
    }

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Optional<CompanyEntity> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    public Optional<CompanyEntity> updateCompany(Long id, CompanyEntity company) {
        return companyRepository.findById(id).map(companyEntity -> {
            companyEntity.setName(company.getName());
            return companyRepository.save(companyEntity);
        });
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }
}
