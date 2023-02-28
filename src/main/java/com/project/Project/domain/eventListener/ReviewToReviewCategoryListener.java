package com.project.Project.domain.eventListener;

import com.project.Project.domain.history.ReviewToReviewCategoryHistory;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.history.ReviewToReviewCategoryHistoryRepository;
import com.project.Project.util.ApplicationContextServe;

import javax.persistence.PreRemove;

public class ReviewToReviewCategoryListener implements EventListener {
    @PreRemove
    public void preRemove(Object o) {
        if (o instanceof ReviewToReviewCategory) {
            ReviewToReviewCategory rtrc = (ReviewToReviewCategory) o;
            ReviewToReviewCategoryHistoryRepository reviewHistoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewToReviewCategoryHistoryRepository.class);
            ReviewToReviewCategoryHistory rtrcHistory = ReviewToReviewCategoryHistory.toReviewToReviewCategoryHistory(rtrc);
            reviewHistoryRepository.save(rtrcHistory);
        }
    }
}
