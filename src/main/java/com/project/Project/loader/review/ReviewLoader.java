package com.project.Project.loader.review;

import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import com.project.Project.domain.review.ReviewToReviewCategory;
import com.project.Project.repository.review.ReviewCategoryRepository;
import com.project.Project.repository.review.ReviewKeywordRepository;
import com.project.Project.repository.review.ReviewToReviewCategoryRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.List;

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
        targetList.stream().map(Review::getReviewToReviewCategoryList).forEach(Hibernate::initialize);
        targetList.stream().map(Review::getReviewToReviewCategoryList)
                .flatMap((list) -> list.stream())
                .map(ReviewToReviewCategory::getReviewCategory).forEach(Hibernate::initialize);

        return targetList;
    }

    public Review loadAllRelations(Review review) {
        review.getReviewToReviewKeywordList().forEach(Hibernate::initialize);
        review.getReviewToReviewCategoryList().forEach(Hibernate::initialize);
        review.getReviewLikeList().forEach(Hibernate::initialize);
        Hibernate.initialize(review.getReviewSummary());
        return review;
    }

    public Review loadBuilding(Review review) {
        Building building = review.getBuilding();
        return review;
    }

}
