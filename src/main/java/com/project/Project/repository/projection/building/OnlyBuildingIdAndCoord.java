package com.project.Project.repository.projection.building;

import com.project.Project.domain.embedded.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder @Getter @AllArgsConstructor
public class OnlyBuildingIdAndCoord {
    private Coordinate coordinate;
    private Long id;
}
