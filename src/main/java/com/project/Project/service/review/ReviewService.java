package com.project.Project.service.review;

import com.project.Project.controller.review.dto.ReviewRequestDto;
import com.project.Project.domain.Member;
import com.project.Project.domain.review.Review;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewListByBuildingId(Long buildingId, Long cursorId, Pageable page);

    List<Review> getReviewListByRoomId(Long roomId, Long cursorId, Pageable page);

    Long deleteById(Long reviewId);

    Review createReview(ReviewRequestDto.ReviewCreateDto request, Member author);

    Long save(Review review);
}
