package com.project.Project.domain.interaction;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.building.Building;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/*
찜기능용 연결 테이블
 */
@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@SQLDelete(sql = "UPDATE member_room SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class Favorite extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
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

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
