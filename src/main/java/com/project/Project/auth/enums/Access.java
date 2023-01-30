package com.project.Project.auth.enums;

public enum Access {

    ALLOW("Allow"),
    DENY("Deny");

    private String name;
    
    Access(String name) {
        this.name = name;
    }
}
