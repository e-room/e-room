package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum MessageStatus {
    READ("읽음"), UNREAD("읽지 않음");

    private String description;

    MessageStatus(String description) {
        this.description = description;
    }
}
