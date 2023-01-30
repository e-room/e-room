package com.project.Project.auth.enums;

public enum ResourceType {
    ENTITY("Entity"),
    OBJECT("Object");

    private String name;

    ResourceType(String name) {
        this.name = name;
    }
}
