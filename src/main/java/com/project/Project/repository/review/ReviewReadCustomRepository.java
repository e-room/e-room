package com.project.Project.repository.review;

import com.project.Project.domain.review.ReviewRead;

import java.util.List;

public interface ReviewReadCustomRepository {

    List<ReviewRead> findReviewsByBuildingId(Long memberId, Long buildingId);
}
