package com.project.Project.service.review;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewRead;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewListByBuildingId(Long buildingId, List<Double> cursorIds, Pageable page);

    Long deleteById(Long reviewId);

    /**
     * if the review exists, throw error
     * else create review
     */
    Review saveReview(ReviewRequestDto.ReviewCreateDto request, Member author, Building building);

    public Review getReviewById(Long reviewId);

    public ReviewRead readReview(Long reviewId, Long memberId);

    public Integer getReviewReadCount(Long memberId);

    public List<ReviewRead> getReadReviews(Long memberId);

    public List<ReviewRead> getReadReviews(Long memberId, Long BuildingId);


    Long save(Review review);
}
