package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.review.ReviewToReviewKeyword;
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
public class ReviewToReviewKeywordHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;


    private Long reviewToReviewKeywordId;

    private Long reviewId;

    private Long reviewKeywordId;

    public static ReviewToReviewKeywordHistory toReviewToReviewKeywordHistory(ReviewToReviewKeyword rtrk) {
        return ReviewToReviewKeywordHistory.builder()
                .id(rtrk.getId())
                .reviewToReviewKeywordId(rtrk.getId())
                .reviewId(rtrk.getReview().getId())
                .reviewId(rtrk.getReviewKeyword().getId()).build();
    }

}
