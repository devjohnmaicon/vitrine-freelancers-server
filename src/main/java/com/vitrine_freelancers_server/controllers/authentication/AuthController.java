package com.vitrine_freelancers_server.controllers.authentication;

import com.vitrine_freelancers_server.controllers.authentication.requests.LoginRequest;
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
    public ResponseEntity<?> register(@RequestBody RegisterUserCompanyDTO userCompanyDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(registerCompanyService.registerUserAndCompany(userCompanyDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest requestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.login(requestDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
