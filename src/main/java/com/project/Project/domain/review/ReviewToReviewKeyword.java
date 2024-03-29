package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.eventListener.ReviewToReviewKeywordListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@EntityListeners(value = ReviewToReviewKeywordListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewToReviewKeyword extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_keyword_id")
    private ReviewKeyword reviewKeyword;

    public void setReview(Review review) {
        if (this.review != null) { // 기존에 이미 팀이 존재한다면
            this.review.getReviewToReviewKeywordList().remove(this); // 관계를 끊는다.
        }
        this.review = review;
        review.getReviewToReviewKeywordList().add(this);
    }

    public void setReviewKeyword(ReviewKeyword reviewKeyword) {
        if (this.reviewKeyword != null) {
            this.reviewKeyword.getReviewList().remove(this);
        }
        this.reviewKeyword = reviewKeyword;
        reviewKeyword.getReviewList().add(this);
    }
}
