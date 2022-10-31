package com.project.Project.controller.building.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BuildingOptionalDto {

    private String buildingName;

    @NotNull
    private Boolean hasElevator;
}
