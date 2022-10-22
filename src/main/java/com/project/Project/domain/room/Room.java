package com.project.Project.domain.room;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.building.Building;
import com.project.Project.domain.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE room SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "UniqueBuildingAndRoomNumber", columnNames = {"building_id", "roomNumber"})
        }
)
public class Room extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "room")
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    /**
     * 몇 동인지
     **/
    @Column
    private Integer lineNumber;

    /**
     * 호실
     * 몇 호실인지
     */
    @NotNull
    @Column
    private Integer roomNumber;

    /**
     * 전용면적
     * 최대 유효 자릿수 : 10, 소수점 우측 자릿수 : 3
     */

    @PreRemove
    public void deleteHandler() {
        super.setDeleted(true);
    }

    // TODO : 주변 상권(동영상) 관련 필드 추가 여부 질문

    public void setBuilding(Building building) {
        if (this.building != null) { // 기존에 이미 팀이 존재한다면
            this.building.getRoomList().remove(this); // 관계를 끊는다.
        }
        this.building = building;
//        building.getRoomList().add(this);
    }
}
