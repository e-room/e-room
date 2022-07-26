package com.project.Project.controller.building.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Coordinate {
    private Double longitude;
    private Double latitude;
}
