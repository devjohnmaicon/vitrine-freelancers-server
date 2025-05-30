package com.vitrine_freelancers_server.domain;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserEntityTest {


    @Test
    public void sholdReturnAuthorities() {
        UserEntity adminUser = UserEntity.builder()
                .email("admin@example.com")
                .role(com.vitrine_freelancers_server.enums.UserRole.ADMIN)
                .build();

        UserEntity companyUser = UserEntity.builder()
                .email("company@example.com")
                .role(com.vitrine_freelancers_server.enums.UserRole.COMPANY)
                .build();

        var adminAuthorities = adminUser.getAuthorities();
        var companyAuthorities = companyUser.getAuthorities();

        assertTrue(
                adminAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
        );
        assertTrue(
                adminAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))
        );
        assertEquals(2, adminAuthorities.size());

        assertTrue(
                companyAuthorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))
        );
        assertEquals(1, companyAuthorities.size());
    }
}
