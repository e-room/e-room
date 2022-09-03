package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.AdvantageKeywordEnum;
import com.project.Project.domain.review.ReviewForm;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE advantage_keyword SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class AdvantageKeyword extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private AdvantageKeywordEnum advantageKeywordEnum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_form_id")
    private ReviewForm reviewForm;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
