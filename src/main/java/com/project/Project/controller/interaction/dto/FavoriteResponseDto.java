package com.project.Project.controller.interaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class FavoriteResponseDto {
    // todo : GET에 대한 응답은 BuildingResponseDto를 사용?

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class FavoriteAddResponse {
        private Long favoriteId;
        private LocalDateTime createdAt;
    }

    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class FavoriteDeleteResponse {
        private Long favoriteId;
        private LocalDateTime deletedAt;
    }
}
