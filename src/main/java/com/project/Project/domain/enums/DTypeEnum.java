package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum DTypeEnum {
    ADVANTAGE("장점"), DISADVANTAGE("단점");

    private String description;

    DTypeEnum(String description) {
        this.description = description;
    }
}
