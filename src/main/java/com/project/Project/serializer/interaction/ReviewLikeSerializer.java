package com.project.Project.serializer.interaction;

import com.project.Project.controller.interaction.dto.ReviewLikeResponseDto;
import com.project.Project.domain.interaction.ReviewLike;

import java.time.LocalDateTime;

public class ReviewLikeSerializer {

    public static ReviewLikeResponseDto.ReviewLikeUpdateResponse toReviewLikeUpdateResponse(ReviewLike updatedReviewLike) {
        return ReviewLikeResponseDto.ReviewLikeUpdateResponse.builder()
                .reviewLikeId(updatedReviewLike.getId())
                .updatedAt(LocalDateTime.now())
                .reviewLikeStatus(updatedReviewLike.getReviewLikeStatus())
                .build();
    }
}
