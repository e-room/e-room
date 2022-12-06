package com.project.Project.controller.review.dto;

import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewScoreDto {

    /**
     * 교통점수
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double traffic;

    /**
     * 건물 및 단지 점수
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double buildingComplex;

    /**
     * 주변 및 환경 점수
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double surrounding;

    /**
     * 내부 점수
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double internal;

    /**
     * 생활 및 입지 점수
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double livingLocation;

    /**
     * 해당 거주지 만족도
     */
    @NotNull
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "5.0")
    private Double residenceSatisfaction;
}
