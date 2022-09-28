package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.BuildingToReviewCategory;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@SQLDelete(sql = "UPDATE review_category SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class ReviewCategory extends BaseEntity {
    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "reviewCategory", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewToReviewCategoryList = new ArrayList<>();

    @OneToMany(mappedBy = "reviewCategory", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<BuildingToReviewCategory> buildingToReviewCategoryList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ReviewCategoryEnum type;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
