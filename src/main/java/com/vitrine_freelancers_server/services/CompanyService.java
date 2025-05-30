package com.vitrine_freelancers_server.services;

import com.vitrine_freelancers_server.controllers.authentication.CreateCompanyDTO;
import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.exceptions.CompanyAlreadyExistsException;
import com.vitrine_freelancers_server.exceptions.CompanyNotFoundException;
import com.vitrine_freelancers_server.exceptions.UserNotAuthorizationException;
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
    public CompanyEntity createCompany(CreateCompanyDTO companyDTO, UserEntity user) {
        try {
            checkIfCompanyExists(companyDTO.name());
            return companyRepository.save(CompanyMapper.toEntity(companyDTO, user));
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(e.getLocalizedMessage());
        }
    }

    public List<CompanyEntity> getAllCompanies() {
        return companyRepository.findAll();
    }

    public void checkIfExistsCompanyByForUser(UserEntity user) {
        if (companyRepository.findByUser_Id(user.getId())) throw new RuntimeException("Já existe uma empresa cadastrada para este usuário");
    }

    public void checkIfCompanyExists(String name) {
        if (companyRepository.existsByName(name)) throw new CompanyAlreadyExistsException();
    }

    public CompanyEntity companyById(Long id) {
        return companyRepository.findById(id).orElseThrow(() -> new CompanyNotFoundException("ID: " + id + " - Empresa não encontrada"));
    }

    public CompanyEntity findCompanyByUser(UserEntity user) {
        return companyRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Empresa não encontrada"));
    }

    public CompanyEntity updateCompany(Long id, CreateCompanyDTO companyRequests, UserEntity user) {
        CompanyEntity company = companyById(id);
        if (!company.getUser().getId().equals(user.getId())) {
            throw new UserNotAuthorizationException();
        }
        company.setName(companyRequests.name());
        return companyRepository.save(company);
    }

    public void inactivatedCompany(Long id) {
        CompanyEntity company = companyById(id);
        company.setName(company.getName() + " - Desativada");
        company.setIsActive(false);
        companyRepository.save(company);
    }
}
