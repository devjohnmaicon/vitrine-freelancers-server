package com.vitrine_freelancers_server.infra.security;

import com.vitrine_freelancers_server.domain.UserEntity;
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
        if (token == null && !request.getRequestURI().startsWith("/auth") && !request.getRequestURI().equals("/jobs")
                && !request.getRequestURI().matches("^/jobs/\\d+$") && !request.getRequestURI().matches("^/companies/\\d+$")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token not provided\"}");
            response.getWriter().flush();
            return;
        }
        var userEmail = tokenService.isValidToken(token);

        if (userEmail != null) {
            UserEntity user = userRepository.getUserEntityByEmail(userEmail).orElseThrow(() -> new RuntimeException("Invalid token"));
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
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
