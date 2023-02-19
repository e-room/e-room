package com.project.Project.service.review;

import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewSummaryRepository extends JpaRepository<ReviewSummary, Long> {
    @Modifying
    @Query("update ReviewSummary r SET r.likeCnt = r.likeCnt + 1 where r.review = :review")
    int increaseLikeCntByReview(@Param(value = "review") Review review);

    @Modifying
    @Query("update ReviewSummary r SET r.likeCnt = r.likeCnt - 1 where r.review = :review")
    int decreaseLikeCntByReview(@Param(value = "review") Review review);
}
