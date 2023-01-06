package com.project.Project.domain.history;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.review.ReviewSummary;
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
public class ReviewSummaryHistory extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Long reviewSummaryId;

    private Long reviewId;

    private Integer likeCnt;

    public static ReviewSummaryHistory toReviewSummaryHistory(ReviewSummary summary) {
        return ReviewSummaryHistory.builder()
                .id(summary.getId())
                .reviewSummaryId(summary.getId())
                .reviewId(summary.getReview().getId())
                .likeCnt(summary.getLikeCnt()).build();
    }

}
