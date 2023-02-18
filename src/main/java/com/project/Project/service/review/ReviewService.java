package com.project.Project.service.review;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.review.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewListByBuildingId(Long buildingId, List<Double> cursorIds, Pageable page);

    // List<Review> getReviewListByRoomId(Long roomId, List<Double> cursorIds, Pageable page);

    Long deleteById(Long reviewId);

    /*
    if the review exists, return review
    else create review
     */
    Review saveReview(ReviewRequestDto.ReviewCreateDto request, Member author, Building building);

    Long save(Review review);
}
