package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ReviewCategoryEnum {

    TRAFFIC("교통"),
    BUILDINGCOMPLEX("건물 및 단지"),
    SURROUNDING("주변 및 환경"),
    INTERNAL("내부"),
    LIVINGLOCATION("생활 및 입지");

    private String description;

    ReviewCategoryEnum(String description){
        this.description = description;
    }
}
