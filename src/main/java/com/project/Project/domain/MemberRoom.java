package com.project.Project.domain;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@SQLDelete(sql = "UPDATE member_room SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
public class MemberRoom extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
