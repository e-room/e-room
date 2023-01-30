package com.project.Project.auth.enums;

public enum SubjectType {

    ROLE("Role"),
    MEMBER("Member");

    private String name;

    SubjectType(String name) {
        this.name = name;
    }
}
