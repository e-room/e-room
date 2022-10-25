package com.project.Project.domain.review;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE review_summary SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class ReviewSummary {
    @Id @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "review_id")
    private Review review;

    @PositiveOrZero
    private Integer likeCnt;

    public void setReview(Review review) {
        this.review = review;
    }
}
