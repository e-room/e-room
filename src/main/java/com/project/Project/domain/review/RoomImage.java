package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
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
@SQLDelete(sql = "UPDATE review_image SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class RoomImage extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_form_id")
    private ReviewForm reviewForm;

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    public void setReviewForm(ReviewForm reviewForm) {
        if (this.reviewForm != null) { // 기존에 이미 팀이 존재한다면
            this.reviewForm.getRoomImageList().remove(this); // 관계를 끊는다.
        }
        this.reviewForm = reviewForm;
        reviewForm.getRoomImageList().add(this);
    }
}
