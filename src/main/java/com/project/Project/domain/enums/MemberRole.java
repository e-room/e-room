package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "일반 사용자"),
    GUEST("ROLE_GUEST", "손님");

    private final String key;
    private final String title;

    MemberRole(String key, String title) {
        this.key = key;
        this.title = title;
    }
}
