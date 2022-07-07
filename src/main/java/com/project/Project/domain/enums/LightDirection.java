package com.project.Project.domain.enums;

public enum LightDirection {
    EAST("동향"),
    SOUTH_EAST("남동향"),
    SOUTH("남향"),
    SOUTH_WEST("남서향"),
    WEST("서향"),
    NORTH_WEST("북서향"),
    NORTH("북향"),
    NORTH_EAST("북동향");

    private String description;

    LightDirection(String description) {
        this.description = description;
    }
}
