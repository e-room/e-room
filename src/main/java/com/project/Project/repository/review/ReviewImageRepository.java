package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);
}
