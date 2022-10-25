package com.project.Project.controller.building.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Builder
@Getter
@AllArgsConstructor
public class CoordinateDto {
    @DecimalMin(value = "0") @DecimalMax(value = "180")
    private Double longitude; // 경도

    @DecimalMin(value = "0") @DecimalMax(value = "90")
    private Double latitude; // 위도
}
