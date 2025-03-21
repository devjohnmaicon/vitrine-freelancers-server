package com.vitrine_freelancers_server.controllers.authentication;

import com.vitrine_freelancers_server.domain.CompanyEntity;
import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.infra.security.TokenService;
import com.vitrine_freelancers_server.services.CompanyService;
import com.vitrine_freelancers_server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterService {
    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    TokenService tokenService;

    @Transactional
    public ResponseTokenDTO registerUserAndCompany(RegisterUserCompanyDTO userCompanyDTO) {
        try {
            UserEntity createdUser = userService.createUser(userCompanyDTO.user());
            CompanyEntity createdCompany = companyService.createCompany(userCompanyDTO.company(), createdUser.getId());
            String token = tokenService.generateToken(createdUser.getEmail());
            ResponseTokenDTO tokenDTO = new ResponseTokenDTO(createdUser.getEmail(), createdCompany.getName(), token);
            return tokenDTO;
        } catch (Exception e) {
            throw new RuntimeException("Error creating user and company:  " + e.getMessage());
        }

    }
}
