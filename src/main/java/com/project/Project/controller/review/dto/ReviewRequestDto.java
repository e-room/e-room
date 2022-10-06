package com.project.Project.controller.review.dto;

import com.project.Project.domain.Member;
import com.project.Project.domain.embedded.AnonymousStatus;
import com.project.Project.domain.enums.*;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.room.Room;
import com.project.Project.validator.ValidEnum;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ReviewRequestDto {
    // todo : 사진 관련 필드 추가 (이미지 업로드 방식 결정 후)
/* todo : 글자수제한, not null 등은 정책을 물어본 후 validation
        필드 : address, advantageDescription, disadvantageDescription
              집크기, 사진관련
    */

    @NoArgsConstructor
    @Getter @Setter
    @AllArgsConstructor
    @Builder
    public static class ReviewCreateDto {

        /**
         * 건물주소 : 경기도 수원시 ~~
         */
        @NotNull
        private String address;

        /**
         * 몇 동인지 : 101동
         */
        @PositiveOrZero
        private Integer lineNumber;

        /**
         * 몇 호인지 : 103호
         */
        @NotNull
        @PositiveOrZero
        private Integer roomNumber;

        /**
         * 거주 유형(아파트 / 오피스텔 또는 원룸 빌라 주택)
         */
        // todo : 답변오면 결정
        @ValidEnum(enumClass = ResidenceType.class, ignoreCase = true)
        private String residenceType;

        /**
         * 거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
         */
        @NotNull
        @ValidEnum(enumClass = ResidencePeriod.class, ignoreCase = true)
        private String residencePeriod;

        /**
         * 거주층 : 저층, 중층, 고층
         */
        @NotNull
        @ValidEnum(enumClass = FloorHeight.class, ignoreCase = true)
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
        @NotNull
        @PositiveOrZero
        private Integer managementFee;

        /**
         * 집 크기 : 6.3평
         */
        @NotNull
        @DecimalMin(value = "0.0")
        private BigDecimal netLeasableArea;

        /**
         * 교통점수
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal traffic;

        /**
         * 건물 및 단지 점수
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal buildingComplex;

        /**
         * 주변 및 환경 점수
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal surrounding;

        /**
         * 내부 점수
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal internal;

        /**
         * 생활 및 입지 점수
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal livingLocation;

        /**
         * 장점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
         * 단열 반려동물 키우기 방충 방음 엘레베이터 조용한동네 평지 마트/편의점 상가
         * (여러개 선택 가능)
         */
        @NotNull
        @Builder.Default
        private List<@ValidEnum(enumClass = KeywordEnum.class, ignoreCase = true) String> advantageKeywordList = new ArrayList<>();

        @NotNull
        @Size(min = 50, max = 500)
        private String advantageDescription;

        /**
         * 단점 키워드 선택 : 없음 주차 대중교통 공원산책 치안 경비실 건물관리 분리수거 환기 방습
         * 단열 반려동물 키우기 벌레 층간소음 엘레베이터 동네소음 언덕 마트/편의점 상가 학교/학원
         */
        @NotNull
        @Builder.Default
        private List<@ValidEnum(enumClass = KeywordEnum.class, ignoreCase = true) String> disadvantageKeywordList = new ArrayList<>();

        @NotNull
        @Size(min = 50, max = 500)
        private String disadvantageDescription;

        @Size(max=5)
        private List<MultipartFile> reviewImageList = new ArrayList<>();

        /**
         * 해당 거주지 만족도
         */
        @NotNull
        @DecimalMin(value = "0.0")
        @DecimalMax(value = "5.0")
        private BigDecimal residenceSatisfaction;
    }
}
