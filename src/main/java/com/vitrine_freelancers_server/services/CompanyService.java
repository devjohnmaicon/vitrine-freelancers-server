package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.mappers.CompanyMapper;
import com.vitrine_freelancers_server.repositories.CompanyRepository;
import com.vitrine_freelancers_server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public CompanyEntity createCompany(CreateCompanyDTO companyDTO, Long userId) {
        try {
            if (findCompanyByName(companyDTO.name()))
                throw new RuntimeException("Already exists a company with this name");
            UserEntity user = userService.findUserById(userId);
            if (companyRepository.findByUser(user).isPresent())
                throw new RuntimeException("O usuário já possui uma empresa cadastrada");
            return companyRepository.save(CompanyMapper.toEntity(companyDTO, user));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getLocalizedMessage());
        }
    }

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public boolean findCompanyByName(String name) {
        return companyRepository.findByNameEquals(name).isPresent();
    }

    public CompanyEntity findCompanyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public CompanyEntity updateCompany(Long id, CreateCompanyDTO companyRequests) {
        CompanyEntity company = findCompanyById(id);
        company.setName(companyRequests.name());
        return companyRepository.save(company);
    }

    public void disableCompany(Long id) {
        CompanyEntity company = findCompanyById(id);
        company.setName(company.getName() + " - Desativada");
        company.setIsActive(false);
        companyRepository.save(company);
    }
}
