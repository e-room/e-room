package com.project.Project.loader.review;

import com.project.Project.domain.review.Review;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.repository.review.ReviewToReviewCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.project.Project.domain.review.QReview.review;
import static com.project.Project.domain.review.QReviewCategory.reviewCategory;
import static com.project.Project.domain.review.QReviewToReviewCategory.reviewToReviewCategory;

@Component
@RequiredArgsConstructor
public class ReviewLoader {

    private final JPAQueryFactory factory;
    private final ReviewToReviewCategoryRepository rtrcRepository;
    private final ReviewKeywordRepository reviewKeywordRepository;
    private final ReviewCategoryRepository reviewCategoryRepository;

    public Review loadAllScores(Review target) {

        return factory.selectFrom(review)
                .innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory)
                .innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory)
                .where(review.id.eq(target.getId()))
                .fetchOne();
    }

    public List<Review> loadAllScores(List<Review> targetList) {

        List<Long> reviewIds = targetList.stream().map(Review::getId).collect(Collectors.toList());

        return factory.selectFrom(review)
                .innerJoin(review.reviewToReviewCategoryList, reviewToReviewCategory)
                .innerJoin(reviewToReviewCategory.reviewCategory, reviewCategory)
                .where(review.id.in(reviewIds)).fetch();
    }
}
