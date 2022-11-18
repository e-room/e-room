package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ReviewLikeStatus {
    ACTIVE("활성"), INACTIVE("비활성");

    private String description;

    ReviewLikeStatus(String description) {
        this.description = description;
    }
}
