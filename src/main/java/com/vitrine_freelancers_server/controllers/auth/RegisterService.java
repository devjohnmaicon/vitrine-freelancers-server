package com.vitrine_freelancers_server.controllers.auth;

import com.vitrine_freelancers_server.controllers.auth.requests.RegisterUserCompanyDTO;
import com.vitrine_freelancers_server.controllers.auth.response.ResponseToken;
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
    public ResponseToken registerUserAndCompany(RegisterUserCompanyDTO userCompanyDTO) {
        UserEntity createdUser = userService.createUser(userCompanyDTO.user());
        CompanyEntity createdCompany = companyService.createCompany(userCompanyDTO.company(), createdUser);

        String token = tokenService.generateToken(createdUser.getEmail());

        return new ResponseToken(createdUser.getEmail(), createdCompany.getName(), token);
    }
}
