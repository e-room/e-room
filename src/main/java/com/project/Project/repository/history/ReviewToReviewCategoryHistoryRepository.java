package com.project.Project.repository.history;

import com.project.Project.domain.history.ReviewSummaryHistory;
import com.project.Project.domain.history.ReviewToReviewCategoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewToReviewCategoryHistoryRepository extends JpaRepository<ReviewToReviewCategoryHistory, Long> {
}
