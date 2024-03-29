package com.project.Project.domain.building;

import com.project.Project.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@Entity
public class BuildingSummary extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private Double avgScore = 0D;

    @Column(nullable = false)
    @ColumnDefault("0")
    @Builder.Default
    private Long reviewCnt = 0L;

    public BuildingSummary() {
        this.building = null;
        this.avgScore = 0D;
        this.reviewCnt = 0L;
    }

    public void updateBuildingSummary(Double avgScore, Long reviewCnt) {
        this.avgScore = avgScore;
        this.reviewCnt = reviewCnt;
    }

    public void setBuilding(Building building) {
        this.building = building;
        building.setBuildingSummary(this);
    }
}
