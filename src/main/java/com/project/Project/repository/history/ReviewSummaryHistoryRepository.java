package com.project.Project.repository.history;

import com.project.Project.domain.history.ReviewHistory;
import com.project.Project.domain.history.ReviewSummaryHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewSummaryHistoryRepository extends JpaRepository<ReviewSummaryHistory, Long> {
}
