package com.project.Project.domain.interaction;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.enums.ReviewLikeStatus;
import com.project.Project.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueLike",
                        columnNames = {
                                "member_id",
                                "review_id"
                        }
                )
        }
)
public class ReviewLike extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column()
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private ReviewLikeStatus reviewLikeStatus = ReviewLikeStatus.LIKED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public void setReviewLikeStatus(ReviewLikeStatus reviewLikeStatus) {
        this.reviewLikeStatus = reviewLikeStatus;
    }

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public void setMember(Member member) {
        if (this.member != null) { // 기존에 이미 팀이 존재한다면
            this.member.getReviewLikeList().remove(this); // 관계를 끊는다.
        }
        this.member = member;
        member.getReviewLikeList().add(this);
    }

    public void setReview(Review review) {
        if (this.review != null) { // 기존에 이미 팀이 존재한다면
            this.review.getReviewLikeList().remove(this); // 관계를 끊는다.
        }
        this.review = review;
        review.getReviewLikeList().add(this);
    }
}
