package com.project.Project.domain.eventListener;

import com.project.Project.domain.history.ReviewSummaryHistory;
import com.project.Project.domain.review.ReviewSummary;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.history.ReviewSummaryHistoryRepository;
import com.project.Project.util.ApplicationContextServe;

import javax.persistence.PreRemove;

public class ReviewSummaryListener implements EventListener {
    @PreRemove
    public void preRemove(Object o) {
        if (o instanceof ReviewSummary) {
            ReviewSummary summary = (ReviewSummary) o;
            ReviewSummaryHistoryRepository reviewSummaryHistoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewSummaryHistoryRepository.class);
            ReviewSummaryHistory reviewHistory = ReviewSummaryHistory.toReviewSummaryHistory(summary);
            reviewSummaryHistoryRepository.save(reviewHistory);
        }
    }
}
