package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum FloorHeight {
    LOW("저층"), MEDIUM("중층"), HIGH("고층");

    private String description;

    FloorHeight(String description) {
        this.description = description;
    }
}
