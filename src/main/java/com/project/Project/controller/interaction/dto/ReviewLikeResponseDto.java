package com.project.Project.controller.interaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ReviewLikeResponseDto {
    @NoArgsConstructor
    @Getter
    @AllArgsConstructor
    @Builder
    public static class ReviewLikeUpdateResponse {
        private Long reviewLikeId;
        private LocalDateTime updatedAt;
    }
}
