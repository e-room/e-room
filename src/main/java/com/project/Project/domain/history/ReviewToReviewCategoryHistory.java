package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.review.ReviewToReviewCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewToReviewCategoryHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long reviewId;

    private Long reviewToReviewCategoryId;

    private Long reviewCategoryId;

    private Double score;

    public static ReviewToReviewCategoryHistory toReviewToReviewCategoryHistory(ReviewToReviewCategory rtrc) {
        return ReviewToReviewCategoryHistory.builder()
                .id(rtrc.getId())
                .reviewToReviewCategoryId(rtrc.getId())
                .reviewId(rtrc.getReview().getId())
                .reviewCategoryId(rtrc.getReviewCategory().getId())
                .score(rtrc.getScore()).build();
    }
}
