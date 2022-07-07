package com.project.Project.domain;

import com.project.Project.domain.embedded.AnonymousStatus;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@SQLDelete(sql = "UPDATE review SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Entity
@Table(
    uniqueConstraints = {
        @UniqueConstraint(name = "UniqueMemberAndRoom", columnNames = {"member_id", "room_id"})
    }
)
public class Review extends BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(fetch = FetchType.LAZY)
    private ReviewForm reviewForm;

    @Embedded
    private AnonymousStatus anonymousStatus;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
