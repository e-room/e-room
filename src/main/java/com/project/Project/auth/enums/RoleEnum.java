package com.project.Project.auth.enums;

import org.springframework.security.core.GrantedAuthority;

public enum RoleEnum implements GrantedAuthority {
    ADMIN("관리자"),
    USER("사용자");

    private String description;

    RoleEnum(String description) {
        this.description = description;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name().toUpperCase();
    }
}
