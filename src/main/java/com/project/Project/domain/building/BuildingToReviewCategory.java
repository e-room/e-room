package com.project.Project.domain.building;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.review.ReviewCategory;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class BuildingToReviewCategory extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    private ReviewCategory reviewCategory;

    private BigDecimal avgScore;
}
