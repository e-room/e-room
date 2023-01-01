package com.project.Project.controller.interaction.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.interaction.dto.ReviewLikeResponseDto;
import com.project.Project.domain.member.Member;
import com.project.Project.serializer.interaction.ReviewLikeSerializer;
import com.project.Project.service.interaction.ReviewLikeService;
import com.project.Project.validator.ExistReview;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Validated
@RestController
@RequiredArgsConstructor
public class ReviewLikeRestController {
    private final ReviewLikeService reviewLikeService;

    @PostMapping("/building/room/review/like/{reviewId}")
    public ResponseEntity<ReviewLikeResponseDto.ReviewLikeUpdateResponse> updateReviewLike(@PathVariable("reviewId") @ExistReview Long reviewId, @AuthUser Member member) {
        Long updatedReviewLikeId = reviewLikeService.updateReviewLike(reviewId, member);
        return ResponseEntity.ok(ReviewLikeSerializer.toReviewLikeUpdateResponse(updatedReviewLikeId));
    }
}
