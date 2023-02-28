package com.project.Project.domain.interaction;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.member.Member;
import com.project.Project.domain.building.Building;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/*
찜기능용 연결 테이블
 */
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UniqueMemberAndBuilding",
                        columnNames = {
                                "member_id",
                                "building_id"
                        }
                )
        }
)
public class Favorite extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    public void setBuilding(Building building) {
        if (this.building != null) { // 기존에 이미 팀이 존재한다면
            this.building.getFavoriteList().remove(this); // 관계를 끊는다.
        }
        this.building = building;
        building.getFavoriteList().add(this);
    }



    public void setMember(Member member) {
        if (this.member != null) { // 기존에 이미 팀이 존재한다면
            this.member.getFavoriteBuildingList().remove(this); // 관계를 끊는다.
        }
        this.member = member;
        member.getFavoriteBuildingList().add(this);
    }

    /**
     * Favorite 엔티티 삭제 전, 연관관계를 끊는 메소드
     */

    public void deleteMemberAndBuilding() {
        this.member = null;
        this.building = null;
    }
}
