package com.vitrine_freelancers_server.controllers.auth;

import com.vitrine_freelancers_server.controllers.auth.requests.LoginRequest;
import com.vitrine_freelancers_server.controllers.auth.requests.RegisterUserCompanyDTO;
import com.vitrine_freelancers_server.controllers.auth.response.ResponseLogin;
import com.vitrine_freelancers_server.controllers.auth.response.ResponseToken;
import com.vitrine_freelancers_server.exceptions.response.ResponseSuccess;
import com.vitrine_freelancers_server.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RegisterService registerCompanyService;

    @PostMapping("/register")
    public ResponseEntity<ResponseSuccess> register(@RequestBody RegisterUserCompanyDTO userCompanyDTO) {
        ResponseToken response = registerCompanyService.registerUserAndCompany(userCompanyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ResponseSuccess("Usuário e empresa criados com sucesso", HttpStatus.CREATED.value(), response)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseSuccess> login(@RequestBody LoginRequest requestDTO) {
        ResponseLogin response = userService.login(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseSuccess("Login realizado com sucesso", HttpStatus.OK.value(), response)
        );
    }

}
