package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ReviewLikeStatus {
    LIKED("좋아요"), CANCELED("취소됨");

    private String description;

    ReviewLikeStatus(String description) {
        this.description = description;
    }

    public ReviewLikeStatus reverse() {
        return this == ReviewLikeStatus.LIKED ? CANCELED : LIKED;
    }

}
