package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Uuid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ReviewImage extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String fileName;

    private String url;

    @OneToOne()
    @JoinColumn(name = "uuid_id")
    private Uuid uuid;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "review")
    private Review review;

    @PreRemove
    public void deleteHandler() {
        this.review = null;
        super.setDeleted(true);
    }

    public void setReview(Review review) {
        if (this.review != null) { // 기존에 이미 팀이 존재한다면
            this.review.getReviewImageList().remove(this); // 관계를 끊는다.
        }
        this.review = review;
        review.getReviewImageList().add(this);
    }
}
