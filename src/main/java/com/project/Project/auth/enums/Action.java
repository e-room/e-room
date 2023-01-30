package com.project.Project.auth.enums;

public enum Action {

    ALL("All"),
    CREATE("Create"),
    READ("Read"),
    UPDATE("Update"),
    DELETE("Delete"),
    NONE("None");

    private String name;


    Action(String name) {
        this.name = name;
    }
}
