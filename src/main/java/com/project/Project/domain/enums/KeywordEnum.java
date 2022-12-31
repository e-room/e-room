package com.project.Project.domain.enums;

import lombok.Getter;

@Getter
public enum KeywordEnum {

    PARKING("주차"),  PUBLIC_TRANSPORTATION("대중교통"), PARK_WALK("공원산책"), SECURITY("치안"),
    SECURITY_OFFICE("경비실"), BUILDING_MANAGEMENT("건물관리"), SEPARATE_TRASH("분리수거"),
    VENTILATION("환기"), MOISTURE_PROOF("방습"), INSULATION("단열"), DAY_LIGHTING("채광"),
    PET_RAISING("반려동물 키우기"), INSECT("벌레"), FLOOR_NOISE("층간소음"),
    ELEVATOR("엘리베이터"), TOWN_NOISE("동네소음"), HILL("언덕"),
    MART_CONVENIENCE_STORE("마트/편의점"), STORE("상가"), SCHOOL_ACADEMY("학교/학원");
    private String description;

    KeywordEnum(String description) {
        this.description = description;
    }
}
