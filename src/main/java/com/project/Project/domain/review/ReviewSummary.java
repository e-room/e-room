package com.project.Project.domain.review;


import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.eventListener.ReviewSummaryListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EntityListeners(value = ReviewSummaryListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewSummary extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "review_id")
    private Review review;

    private Integer likeCnt;

    public void setReview(Review review) {
        this.review = review;
    }
}
