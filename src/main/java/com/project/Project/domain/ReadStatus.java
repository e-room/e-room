package com.project.Project.domain;

import lombok.Getter;

@Getter
public enum ReadStatus {
    READ("읽음"), UNREAD("읽지 않음");

    private String description;

    ReadStatus(String description) {
        this.description = description;
    }
}
