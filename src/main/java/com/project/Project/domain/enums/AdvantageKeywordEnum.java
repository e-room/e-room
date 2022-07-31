package com.project.Project.domain.enums;

import lombok.Getter;

/**
 * 장점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
 *                단열 반려동물 키우기 방충 방음 엘레베이터 조용한동네 평지 마트/편의점 상가
 */
@Getter
public enum AdvantageKeywordEnum {

    NONE("없음"), PARKING("주차"),  PUBLIC_TRANSPORTATION("대중교통"),
    PARK_WALK("공원산책"), SECURITY("치안"), SECURITY_OFFICE("경비실"),
    BUILDING_MANAGEMENT("건물관리"), SEPARATE_TRASH("분리수거"), VENTILATION("환기"),
    MOISTURE_PROOF("방습"), INSULATION("단열"), PET_RAISING("반려동물 키우기"),
    INSECT_PROOF("방충"), SOUND_PROOF("방음"), ELEVATOR("엘리베이터"),
    QUIET_TOWN("조용한 동네"), FLAT_GROUND("평지"), MART_CONVENIENCE_STORE("마트/편의점"),
    STORE("상가"), SCHOOL_ACADEMY("학교/학원"),DAY_LIGHTING("채광");

    private String description;

    AdvantageKeywordEnum(String description) {
        this.description = description;
    }
}
