package com.project.Project.repository.review;

import com.project.Project.domain.review.Review;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface ReviewCustomRepository {
    long updateReview(Review review);

    long softDeleteReview(Review review);

    Optional<Review> findById(Long reviewId);

    List<Review> findReviewsByBuildingId(Long buildingId, List<Double> cursorIds, Pageable pageable);

    Function<Long, JPAQuery<Review>> findReviewQueryByBuildingId(List<Double> cursorIds, Pageable pageable);

    List<OrderSpecifier<?>> getAllOrderSpecifiers(Pageable pageable);


}
