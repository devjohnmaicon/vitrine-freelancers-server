package com.vitrine_freelancers_server.dtos.User;

public class UserPrincipalDTO {
    private Long id;
    private String email;
    private String name;
    private Long companyId;
    private String authorities;

    public UserPrincipalDTO(Long id, String email, String name, Long companyId, String authorities) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.companyId = companyId;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAuthorities() {
        return authorities;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
