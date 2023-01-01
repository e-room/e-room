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
public class Favorite extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Building building;

    /**
     * Favorite 엔티티 삭제 전, 연관관계를 끊는 메소드
     */

    public void deleteMemberAndBuilding() {
        this.member = null;
        this.building = null;
    }
}
