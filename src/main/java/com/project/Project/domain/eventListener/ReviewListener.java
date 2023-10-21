package com.project.Project.domain.eventListener;

import com.project.Project.domain.history.ReviewHistory;
import com.project.Project.domain.review.Review;
import com.project.Project.repository.EventListener;
import com.project.Project.repository.building.BuildingSummaryRepository;
import com.project.Project.repository.building.BuildingToReviewCategoryRepository;
import com.project.Project.repository.history.ReviewHistoryRepository;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewRepository;
import com.project.Project.repository.review.ReviewToReviewCategoryRepository;
import com.project.Project.common.util.ApplicationContextServe;

import javax.annotation.PostConstruct;
import javax.persistence.PreRemove;

public class ReviewListener implements EventListener {

    private ReviewRepository reviewRepository;
    private BuildingSummaryRepository buildingSummaryRepository;
    private BuildingToReviewCategoryRepository buildingToReviewCategoryRepository;
    private ReviewToReviewCategoryRepository reviewToReviewCategoryRepository;
    private ReviewCategoryRepository reviewCategoryRepository;

    @PostConstruct
    public void init() {
        this.reviewRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewRepository.class);
        this.buildingSummaryRepository = ApplicationContextServe.getApplicationContext().getBean(BuildingSummaryRepository.class);
        this.buildingToReviewCategoryRepository = ApplicationContextServe.getApplicationContext().getBean(BuildingToReviewCategoryRepository.class);
        this.reviewToReviewCategoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewToReviewCategoryRepository.class);
        this.reviewCategoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewCategoryRepository.class);
    }

    @PreRemove
    public void preRemove(Object o) {
        if (o instanceof Review) {
            Review review = (Review) o;
            ReviewHistoryRepository reviewHistoryRepository = ApplicationContextServe.getApplicationContext().getBean(ReviewHistoryRepository.class);
            ReviewHistory reviewHistory = ReviewHistory.toReviewHistory(review);
            ReviewHistory savedReviewHistory = reviewHistoryRepository.save(reviewHistory);
//            reviewHistoryRepository.flush();
            savedReviewHistory.getId();
        }
    }
}
