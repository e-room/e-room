package com.project.Project.serializer.interaction;

import com.project.Project.controller.interaction.dto.ReviewLikeResponseDto;

import java.time.LocalDateTime;

public class ReviewLikeSerializer {

    public static ReviewLikeResponseDto.ReviewLikeUpdateResponse toReviewLikeUpdateResponse(Long updatedReviewLikeId) {
        return ReviewLikeResponseDto.ReviewLikeUpdateResponse.builder()
                .reviewLikeId(updatedReviewLikeId)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
