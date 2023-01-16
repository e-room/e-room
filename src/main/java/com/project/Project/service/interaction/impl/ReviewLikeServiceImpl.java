package com.project.Project.service.interaction.impl;

import com.project.Project.domain.member.Member;
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

    @Transactional
    public ReviewLike updateReviewLike(Long reviewId, Member member) {
        Optional<ReviewLike> optionalReviewLike = reviewLikeRepository.findByMemberAndReview_Id(member, reviewId);
        ReviewLike reviewLike;
        if(optionalReviewLike.isPresent()) {
            reviewLike = optionalReviewLike.get();
            reviewLike.setReviewLikeStatus(reviewLike.getReviewLikeStatus().reverse());
        } else {
            reviewLike = ReviewLike.builder()
                    .reviewLikeStatus(ReviewLikeStatus.LIKED)
                    .build();
            reviewLike.setReview(reviewRepository.findById(reviewId).get());
            reviewLike.setMember(member);
            reviewLike = reviewLikeRepository.save(reviewLike);
        }
        return reviewLike;
    }
}
