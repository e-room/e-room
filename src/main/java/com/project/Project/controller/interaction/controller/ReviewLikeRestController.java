package com.project.Project.controller.interaction.controller;

import com.project.Project.auth.AuthUser;
import com.project.Project.controller.interaction.dto.ReviewLikeResponseDto;
import com.project.Project.domain.Member;
import com.project.Project.service.interaction.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewLikeRestController {
    private final ReviewLikeService reviewLikeService;

    @PostMapping("/building/room/review/like/{reviewId}")
    public ResponseEntity<ReviewLikeResponseDto.ReviewLikeUpdateResponse> updateReviewLike(@PathVariable("reviewId") Long reviewId, @AuthUser Member member) {
        return null;
    }
}
