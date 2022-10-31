package com.project.Project.controller.review.dto;

import lombok.*;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewBaseDto {
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
    private Double netLeasableArea;
}
