package com.project.Project.controller.member.dto;

import com.project.Project.controller.building.dto.CoordinateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class MemberResponseDto {

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberProfileDto {
        private String name;
        private String email;
        private String profileImageUrl;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class MemberDeleteDto {
        private Long memberId;
        private LocalDateTime deletedAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class RecentMapLocationDto {
        private LocalDateTime updatedAt;
        private CoordinateDto coordinateDto;
    }
}
