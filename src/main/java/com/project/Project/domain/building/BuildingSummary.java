package com.project.Project.domain.building;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.review.ReviewCategory;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
public class BuildingSummary extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private Building building;

    @OneToMany()
    @Builder.Default
    private List<ReviewCategory> reviewCategory = new ArrayList<>();

    private Double avgScore;
}
