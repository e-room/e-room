package com.project.Project.domain.eventListener;

import com.project.Project.domain.history.ReviewToReviewKeywordHistory;
import com.project.Project.domain.review.ReviewToReviewKeyword;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.history.ReviewToReviewKeywordHistoryRepository;
import com.project.Project.common.util.ApplicationContextServe;

import javax.persistence.PreRemove;

public class ReviewToReviewKeywordListener implements EventListener {
    @PreRemove
    public void preRemove(Object o) {
        if (o instanceof ReviewToReviewKeyword) {
            ReviewToReviewKeyword rtrk = (ReviewToReviewKeyword) o;
            ReviewToReviewKeywordHistoryRepository reviewHistoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewToReviewKeywordHistoryRepository.class);
            ReviewToReviewKeywordHistory rtrkHistory = ReviewToReviewKeywordHistory.toReviewToReviewKeywordHistory(rtrk);
            reviewHistoryRepository.save(rtrkHistory);
        }
    }
}
