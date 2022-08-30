package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewListByBuildingId(Long buildingId);
    List<Review> getReviewListByRoomId(Long roomId);

    Long deleteById(Long reviewId);

    Long save(Review review);
}
