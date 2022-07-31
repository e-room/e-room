package com.project.Project.controller.review.dto;

import com.project.Project.domain.enums.*;
import com.project.Project.validator.ValidEnum;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

public class ReviewRequestDto {

    @NoArgsConstructor @Getter @AllArgsConstructor @Builder
    public static class ReviewCreateDto{
        /**
         * 거주 유형(아파트 / 오피스텔 또는 원룸 빌라 주택)
         */
        @ValidEnum(enumClass = ResidenceType.class, ignoreCase = true)
        private String residenceType;

        /**
         * 거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
         */
        @ValidEnum(enumClass = ResidencePeriod.class, ignoreCase = true)
        private String residencePeriod;

        /**
         * 거주층 : 저층, 중층, 고층
         */
        @ValidEnum(enumClass = FloorHeight.class,ignoreCase = true)
        private String floorHeight;

        /**
         * 보증금 : 000만원
         */
        @NotNull
        @PositiveOrZero
        private Integer deposit;

        /**
         * 월세 : 00만원
         */
        @NotNull
        @PositiveOrZero
        private Integer monthlyRent;

        /**
         * 관리비 : 몇호기준 얼마정도에요. 여름에는 에어컨을 틀면 추가적으로 ....
         */
        private String managementFeeDescription;

        /**
         * 교통점수 : 5단계 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String trafficScore;

        /**
         * 건물 및 단지 점수 : 5단계 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String  buildingComplexScore;

        /**
         * 주변 및 환경 점수 : 5단계 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String surroundingScore;

        /**
         * 내부 점수 : 5단계 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String internalScore;

        /**
         * 생활 및 입지 점수 : 5단계 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String LivingLocationScore;

        @Builder.Default
        @Valid
        @ValidEnum(enumClass = AdvantageKeywordEnum.class, ignoreCase = true)
        private List<String> advantageKeywordList = new ArrayList<>();

        /**
         * 단점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
         * 단열 반려동물 키우기 벌레 층간소음 엘레베이터 동네소음 언덕 마트/편의점 상가 학교/학원
         */
        @Builder.Default
        @Valid
        @ValidEnum(enumClass = DisadvantageKeywordEnum.class, ignoreCase = true)
        private List<String> disadvantageKeywordList = new ArrayList<>();

        /**
         * 해당 거주지 만족도 : 별 1개부터 5개까지 선택
         */
        @ValidEnum(enumClass = ScoreOption.class, ignoreCase = true)
        private String residenceSatisfaction;
    }
}
