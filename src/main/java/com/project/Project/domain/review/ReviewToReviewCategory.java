package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@SQLDelete(sql = "UPDATE review_to_review_category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueReviewAndReviewCategory", columnNames = {"review_id", "review_category_id"})
        }
)
public class ReviewToReviewCategory extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_category_id")
    private ReviewCategory reviewCategory;

    private Double score;

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public void setReview(Review review) {
        if (this.review != null) { // 기존에 이미 팀이 존재한다면
            this.review.getReviewToReviewCategoryList().remove(this); // 관계를 끊는다.
        }
        this.review = review;
        review.getReviewToReviewCategoryList().add(this);
    }

    public void setReviewCategory(ReviewCategory reviewCategory) {
        if (this.reviewCategory != null) { // 기존에 이미 팀이 존재한다면
            this.reviewCategory.getReviewToReviewCategoryList().remove(this); // 관계를 끊는다.
        }
        this.reviewCategory = reviewCategory;
        reviewCategory.getReviewToReviewCategoryList().add(this);
    }
}
