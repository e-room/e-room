package com.project.Project.domain.building;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.enums.ReviewCategoryEnum;
import com.project.Project.domain.review.ReviewCategory;
import lombok.*;

import javax.persistence.*;
import java.util.Optional;
import java.util.function.Function;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class BuildingToReviewCategory extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewCategory reviewCategory;

    private Double avgScore;

    public static final Function<BuildingToReviewCategory, ReviewCategoryEnum> bestCategoryOrNull = (reviewCategory) -> Optional.ofNullable(reviewCategory).map(BuildingToReviewCategory::getReviewCategory)
            .map(ReviewCategory::getType)
            .orElse(null);
}
