package com.project.Project.controller.building.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BuildingOptionalDto {

    private String buildingName;
    private Boolean hasElevator;
}
