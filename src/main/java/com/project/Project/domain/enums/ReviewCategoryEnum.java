package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum ReviewCategoryEnum {

    TRAFFIC("교통"),
    BUILDINGCOMPLEX("건물 및 단지"),
    SURROUNDING("주변 및 환경"),
    INTERNAL("내부"),
    LIVINGLOCATION("생활 및 입지"),
    RESIDENCESATISFACTION("해당 거주지 만족도");
    private String description;

    ReviewCategoryEnum(String description) {
        this.description = description;
    }

    public static boolean contains(String test) {

        for (ReviewCategoryEnum r : ReviewCategoryEnum.values()) {
            if (r.name().equalsIgnoreCase(test)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(ReviewCategoryEnum test) {
        for (ReviewCategoryEnum r : ReviewCategoryEnum.values()) {
            if (r.equals(test)) {
                return true;
            }
        }
        return false;
    }
}
