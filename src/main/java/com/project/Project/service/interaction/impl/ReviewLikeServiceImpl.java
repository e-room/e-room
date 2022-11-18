package com.project.Project.service.interaction.impl;

import com.project.Project.domain.Member;
import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.interaction.ReviewLike;
import com.project.Project.repository.interaction.ReviewLikeRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.service.interaction.ReviewLikeService;
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

    private ReviewLikeStatus getReverseReviewLikeStatus(ReviewLikeStatus reviewLikeStatus) {
        return reviewLikeStatus == ReviewLikeStatus.ACTIVE ? ReviewLikeStatus.INACTIVE : ReviewLikeStatus.ACTIVE;
    }

    @Transactional
    public Long updateReviewLike(Long reviewId, Member member) {
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findByMemberAndReview_Id(member, reviewId);
        ReviewLike reviewLike;
        if(optionalReviewLike.isPresent()) {
            reviewLike = optionalReviewLike.get();
            reviewLike.setReviewLikeStatus(getReverseReviewLikeStatus(reviewLike.getReviewLikeStatus()));
        } else {
            reviewLike = ReviewLike.builder()
                    .reviewLikeStatus(ReviewLikeStatus.ACTIVE)
                    .build();
            reviewLike.setReview(reviewRepository.findById(reviewId).get());
            reviewLike.setMember(member);
            reviewLikeRepository.save(reviewLike);
        }
        return reviewLike.getId();
    }
}
