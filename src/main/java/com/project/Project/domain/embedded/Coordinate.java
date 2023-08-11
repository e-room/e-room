package com.project.Project.domain.embedded;

import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.building.BuildingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Coordinate {

    private Double latitude;

    private Double longitude;

    public static CoordinateDto toCoordinateDto(Coordinate coordinate) {
        Optional.ofNullable(coordinate).orElseThrow(() -> new BuildingException("좌표 객체가 null입니다.", ErrorCode.COORDINATE_BAD_REQUEST));
        return CoordinateDto.builder()
                .latitude(coordinate.latitude)
                .longitude(coordinate.longitude)
                .build();
    }
}
