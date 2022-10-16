package com.project.Project.domain.building;

import com.project.Project.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE building_summary SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class BuildingSummary {
    @Id @GeneratedValue
    private Long id;

    @OneToOne()
    @JoinColumn(name = "building_id")
    private Building building;

    private Double avgScore;

    private Long reviewCnt;

    public BuildingSummary(){
        this.building = null;
        this.avgScore = 0.0;
        this.reviewCnt = 0L;
    }

    public void setBuilding(Building building) {
        this.building = building;
        building.setBuildingSummary(this);
    }
}
