package com.project.Project.controller.building.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class CoordinateDto {
    private Double longitude;
    private Double latitude;
}
