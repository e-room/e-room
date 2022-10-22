package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;

public interface ReviewCustomRepository {
    long updateReview(Review review);

    long softDeleteReview(Review review);
}
