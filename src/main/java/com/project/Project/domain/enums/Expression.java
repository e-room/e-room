package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum Expression {

    POSITIVE("좋아요"), NEGATIVE("별로에요"), NONE("응답안함");

    private String description;

    Expression(String description) {
        this.description = description;
    }
}
