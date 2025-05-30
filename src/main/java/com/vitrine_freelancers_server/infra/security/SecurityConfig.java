package com.vitrine_freelancers_server.infra.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                                .requestMatchers(HttpMethod.GET, "/jobs/{id}").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/jobs/{id}").hasAnyRole("ADMIN", "COMPANY")
                                .requestMatchers(HttpMethod.DELETE, "/jobs/{id}").hasAnyRole("ADMIN", "COMPANY")
                                .requestMatchers(HttpMethod.GET, "/jobs").permitAll()
                                .requestMatchers(HttpMethod.POST, "/jobs").hasAnyRole("ADMIN", "COMPANY")
                                .requestMatchers("/jobs/**").hasAnyRole("ADMIN")

//                        .requestMatchers(HttpMethod.GET, "/jobs/**").permitAll()
//                        .requestMatchers(HttpMethod.GET, "/jobs/company").hasAnyRole("ADMIN", "COMPANY")
//
                                .requestMatchers("/users/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.GET, "/users/:id").hasAnyRole("ADMIN", "USER")
//
                                .requestMatchers(HttpMethod.GET, "/companies/{id}").permitAll()
                                .requestMatchers("/companies/**").hasRole("ADMIN")
//                        .requestMatchers(HttpMethod.PUT, "/companies/:id").hasAnyRole("ADMIN", "COMPANY")
                                .anyRequest().authenticated()
                )
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                })
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .cors(
                        httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(request -> {
                            var cors = new org.springframework.web.cors.CorsConfiguration();
                            cors.applyPermitDefaultValues();
                            cors.addAllowedOrigin("http://localhost:3000");
                            cors.checkOrigin("http://localhost:3000");
                            return cors;
                        })

                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}