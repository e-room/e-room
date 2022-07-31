package com.project.Project.domain.review;

import com.project.Project.domain.BaseEntity;
import com.project.Project.domain.Member;
import com.project.Project.domain.interaction.Like;
import com.project.Project.domain.room.Room;
import com.project.Project.domain.embedded.AnonymousStatus;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter @NoArgsConstructor @AllArgsConstructor @Builder
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

    @OneToMany(mappedBy = "review")
    @Builder.Default
    private List<Like> likeMemberList = new ArrayList<>();

    private Integer likeCnt;

    @OneToOne(fetch = FetchType.LAZY)
    private ReviewForm reviewForm;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ReviewToReviewCategory> reviewSummaryList = new ArrayList<>();

    @Embedded
    private AnonymousStatus anonymousStatus;

    @PreRemove
    public void deleteHandler(){
        super.setDeleted(true);
    }
}
