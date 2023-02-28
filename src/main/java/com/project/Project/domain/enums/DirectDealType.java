package com.project.Project.domain.enums;

public enum DirectDealType {
    POSSIBLE("가능"),
    CHECKING("확인중"),
    IMPOSSIBLE("불가능");

    private String description;

    DirectDealType(String description) {
        this.description = description;
    }
}
