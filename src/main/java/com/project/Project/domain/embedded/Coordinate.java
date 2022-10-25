package com.project.Project.domain.embedded;

import com.project.Project.controller.building.dto.CoordinateDto;
import com.project.Project.exception.ErrorCode;
import com.project.Project.exception.building.BuildingException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class Coordinate {
    @DecimalMin(value = "0") @DecimalMax(value = "90")
    private Double latitude;
    @DecimalMin(value = "0") @DecimalMax(value = "180")
    private Double longitude;

    public static CoordinateDto toCoordinateDto(Coordinate coordinate) {
        Optional.ofNullable(coordinate).orElseThrow(() -> new BuildingException("좌표 객체가 null입니다.", ErrorCode.COORDINATE_BAD_REQUEST));
        return CoordinateDto.builder()
                .latitude(coordinate.latitude)
                .longitude(coordinate.longitude)
                .build();
    }
}
