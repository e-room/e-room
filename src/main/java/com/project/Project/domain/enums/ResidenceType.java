package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ResidenceType {
    APARTMENT("아파트"),
    OFFICE_TEL("오피스텔"),
    ONE_ROOM("원룸"),
    VILLA("빌라"),
    HOUSE("주택");

    private String description;

    ResidenceType(String description) {
        this.description = description;
    }
}
