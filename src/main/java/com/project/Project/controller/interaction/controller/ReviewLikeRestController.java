package com.project.Project.controller.interaction.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.interaction.dto.ReviewLikeResponseDto;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.member.Member;
import com.project.Project.serializer.interaction.ReviewLikeSerializer;
import com.project.Project.service.interaction.ReviewLikeService;
import com.project.Project.validator.ExistReview;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "03-ReviewLike 👍",description = "리뷰 좋아요, 취소")
@Validated
@RestController
@RequiredArgsConstructor
public class ReviewLikeRestController {
    private final ReviewLikeService reviewLikeService;
    @Operation(summary = "리뷰 좋아요/취소 [3.2]", description = "리뷰 좋아요/취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ReviewLikeResponseDto.ReviewLikeUpdateResponse.class))),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED")
    })
    @Parameters({
            @Parameter(name = "reviewId", description = "좋아요/취소하고자하는 리뷰의 id", example = "1021"),
            @Parameter(name = "member", hidden = true)
    })
    @PutMapping("/building/room/review/like/{reviewId}")
    public ResponseEntity<ReviewLikeResponseDto.ReviewLikeUpdateResponse> updateReviewLike(@PathVariable("reviewId") @ExistReview Long reviewId, @AuthUser Member member) {
        ReviewLike updatedReviewLike = reviewLikeService.updateReviewLike(reviewId, member);
        return ResponseEntity.ok(ReviewLikeSerializer.toReviewLikeUpdateResponse(updatedReviewLike));
    }
}
