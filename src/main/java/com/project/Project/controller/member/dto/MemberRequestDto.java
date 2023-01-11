package com.project.Project.controller.member.dto;

import com.project.Project.controller.building.dto.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberRequestDto {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RecentMapLocation {
        private CoordinateDto coordinateDto;
    }
}
