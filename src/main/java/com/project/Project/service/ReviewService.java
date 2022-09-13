package com.project.Project.service;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewListByBuildingId(Long buildingId, Long cursorId, Pageable page);
    List<Review> getReviewListByRoomId(Long roomId, Long cursorId, Pageable page);

    Long deleteById(Long reviewId);

    Long save(Review review);
}
