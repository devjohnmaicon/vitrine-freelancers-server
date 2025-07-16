package com.vitrine_freelancers_server.utils;

import com.vitrine_freelancers_server.domain.*;
import com.vitrine_freelancers_server.dtos.User.UserPrincipalDTO;
import com.vitrine_freelancers_server.enums.JobType;
import com.vitrine_freelancers_server.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MockDataFactory {
    private static final Long COMPANY_ID = 1L;
    private static final Long JOB_ID = 1L;
    private static final Long USER_ID = 1L;

    public static CompanyEntity createCompany(
            UserEntity user,
            List<JobEntity> jobs
    ) {
        return new CompanyEntity(
                COMPANY_ID,
                "Farmácia do João",
                user,
                jobs != null ? jobs : List.of(),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }


    public static UserEntity createUser() {
        return new UserEntity(
                USER_ID,
                "User 1",
                "user@email",
                "123",
                UserStatus.ACTIVE,
                Set.of(new Role()),
                createCompany(null, null),
                LocalDateTime.now(),
                null
        );
    }

    public static Permission createPermission() {
        return new Permission(1L, "PERMISSION_1");
    }

    public static JobEntity createJobEntity(
            String position
    ) {
        return new JobEntity(
                JOB_ID,
                JobType.FREELANCER,
                position == null ? "deliveryman" : position,
                "Vaga para motoentregador",
                "2021-10-10",
                "14:00",
                "18:00",
                100.0,
                "Possuir moto própria",
                true,
                createCompany(null, null),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static UserPrincipalDTO createUserPrincipal() {
        return new UserPrincipalDTO(
                1L,
                "User 1",
                "user@email",
                1L,
                "ROLE_COMPANY"
        );
    }
}
