package com.project.Project.auth.enums;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
public enum MemberRole implements GrantedAuthority {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;

    MemberRole(String key, String title) {
        this.key = key;
        this.title = title;
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + this.name().toUpperCase();
    }
}
