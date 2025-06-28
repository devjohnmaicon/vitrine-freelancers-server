package com.vitrine_freelancers_server.infra.security;

import com.vitrine_freelancers_server.domain.UserEntity;
import com.vitrine_freelancers_server.dtos.User.UserPrincipalDTO;
import com.vitrine_freelancers_server.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    TokenService tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        var userEmail = tokenService.isValidToken(token);

        if (userEmail != null) {
            UserEntity user = userRepository.getUserEntityByEmail(userEmail).orElseThrow(() -> new RuntimeException("Invalid token"));
            UserPrincipalDTO userPrincipalDTO = new UserPrincipalDTO(user.getId(), user.getEmail(), user.getName(), user.getCompany().getId(), user.getAuthorities().toString());
            var authentication = new UsernamePasswordAuthenticationToken(userPrincipalDTO, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}
