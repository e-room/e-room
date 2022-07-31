package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@SQLDelete(sql = "UPDATE review_to_review_category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueReviewAndReviewCategory", columnNames = {"review_id", "review_category_id"})
        }
)
public class ReviewToReviewCategory extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private Review review;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "review_category_id")
    private ReviewCategory reviewCategory;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
