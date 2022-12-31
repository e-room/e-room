package com.project.Project.controller.review.dto;


import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReviewResidencePeriodDto {

    /**
     * 거주 기간 : 2018년 이전, 2018년까지, 2019년까지, ..., 2022년까지
     */
    @NotNull
    private Integer residenceStartYear;

    @NotNull
    private Integer residenceDuration;
}
