package com.project.Project.domain.embedded;

import com.project.Project.controller.building.dto.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Embeddable
public class Coordinate {

    private Double latitude;

    private Double longitude;

    public static CoordinateDto toCoordinateDto(Coordinate coordinate){
        return CoordinateDto.builder()
                .latitude(coordinate.latitude)
                .longitude(coordinate.longitude)
                .build();
    }
}
