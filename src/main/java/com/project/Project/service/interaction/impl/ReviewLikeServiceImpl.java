package com.project.Project.service.interaction.impl;

import com.project.Project.domain.member.Member;
import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.domain.review.Review;
import com.project.Project.common.exception.ErrorCode;
import com.project.Project.common.exception.review.ReviewException;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.interaction.ReviewLikeService;
import com.project.Project.service.review.ReviewSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;

    private final ReviewSummaryRepository reviewSummaryRepository;

    @Transactional
    public ReviewLike updateReviewLike(Long reviewId, Member member) {
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findByMemberAndReview_Id(member, reviewId);
        ReviewLike reviewLike;
        Review review = reviewRepository.findById(reviewId).orElseThrow(() -> new ReviewException(ErrorCode.REVIEW_NOT_FOUND));

        if(optionalReviewLike.isPresent()) {
            reviewLike = optionalReviewLike.get();
            ReviewLikeStatus changedStatus = reviewLike.getReviewLikeStatus().reverse();
            reviewLike.setReviewLikeStatus(changedStatus);
            if(changedStatus == ReviewLikeStatus.LIKED) reviewSummaryRepository.increaseLikeCntByReview(review);
            else reviewSummaryRepository.decreaseLikeCntByReview(review);
        } else {
            reviewLike = ReviewLike.builder()
                    .reviewLikeStatus(ReviewLikeStatus.LIKED)
                    .build();
            reviewLike.setReview(review);
            reviewLike.setMember(member);
            reviewLike = reviewLikeRepository.save(reviewLike);
            reviewSummaryRepository.increaseLikeCntByReview(review);
        }
        return reviewLike;
    }
}
